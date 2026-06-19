import sys, os, pandas as pd, mysql.connector
from mysql.connector import Error

EXCEL_PATH = "Serie-Historica-IFDM-2013-a-2023.xlsx"
ANO = 2023
DB_CONFIG = {"host":"localhost","user":"root","password":"root","charset":"utf8mb4"}
DB_NAME = "ifdm"
ESTADOS = {
    "AC":"Acre","AL":"Alagoas","AM":"Amazonas","AP":"Amapá","BA":"Bahia",
    "CE":"Ceará","DF":"Distrito Federal","ES":"Espírito Santo","GO":"Goiás",
    "MA":"Maranhão","MG":"Minas Gerais","MS":"Mato Grosso do Sul",
    "MT":"Mato Grosso","PA":"Pará","PB":"Paraíba","PE":"Pernambuco",
    "PI":"Piauí","PR":"Paraná","RJ":"Rio de Janeiro","RN":"Rio Grande do Norte",
    "RO":"Rondônia","RR":"Roraima","RS":"Rio Grande do Sul",
    "SC":"Santa Catarina","SE":"Sergipe","SP":"São Paulo","TO":"Tocantins",
}

def to_float(v):
    try:
        f=float(v); return None if f!=f else round(f,6)
    except: return None

def to_int(v):
    try: return int(float(v))
    except: return None

def main():
    if not os.path.exists(EXCEL_PATH):
        print(f"ERRO: coloque {EXCEL_PATH} nesta pasta"); sys.exit(1)
    print(f"Lendo planilha (ano {ANO})...")
    df_g=pd.read_excel(EXCEL_PATH,sheet_name="IFDM Geral")
    df_e=pd.read_excel(EXCEL_PATH,sheet_name="IFDM Educação")
    df_s=pd.read_excel(EXCEL_PATH,sheet_name="IFDM Saúde")
    df_r=pd.read_excel(EXCEL_PATH,sheet_name="IFDM Emprego&Renda")
    df=(df_g[["COD_MUNIC","SIGLA_UF","NOME_MUNIC",
        f"Ranking Estadual IFDM {ANO}",f"Ranking IFDM {ANO}",f"IFDM {ANO}"]]
        .merge(df_e[["COD_MUNIC",f"IFDM Educação {ANO}"]],on="COD_MUNIC")
        .merge(df_s[["COD_MUNIC",f"IFDM Saúde {ANO}"]],on="COD_MUNIC")
        .merge(df_r[["COD_MUNIC",f"IFDM Emprego {ANO}"]],on="COD_MUNIC"))
    df.columns=["cod_munic","sigla_uf","nome","ranking_estadual","ranking_nacional","ifdm","educacao","saude","renda"]
    print(f"{len(df)} municípios carregados.")
    try:
        con=mysql.connector.connect(**DB_CONFIG)
    except Error as e:
        print(f"ERRO MySQL: {e}\nRode ./setup_mysql.sh primeiro."); sys.exit(1)
    cur=con.cursor()
    cur.execute(f"CREATE DATABASE IF NOT EXISTS {DB_NAME} CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    cur.execute(f"USE {DB_NAME}")
    cur.execute("SET FOREIGN_KEY_CHECKS=0")
    cur.execute("DROP TABLE IF EXISTS cidade")
    cur.execute("DROP TABLE IF EXISTS unidade_federacao")
    cur.execute("SET FOREIGN_KEY_CHECKS=1")
    cur.execute("""CREATE TABLE unidade_federacao(
        id INT NOT NULL AUTO_INCREMENT,uf CHAR(2) NOT NULL UNIQUE,
        descricao VARCHAR(60) NOT NULL,media_saude DECIMAL(10,6),
        media_educacao DECIMAL(10,6),media_renda DECIMAL(10,6),
        media_ifdm DECIMAL(10,6),PRIMARY KEY(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""")
    cur.execute("""CREATE TABLE cidade(
        id INT NOT NULL AUTO_INCREMENT,cod_munic INT NOT NULL UNIQUE,
        nome VARCHAR(100) NOT NULL,uf_id INT NOT NULL,
        saude DECIMAL(10,6),educacao DECIMAL(10,6),renda DECIMAL(10,6),
        ifdm DECIMAL(10,6),ranking_nacional INT,ranking_estadual INT,
        PRIMARY KEY(id),
        CONSTRAINT fk_cidade_uf FOREIGN KEY(uf_id) REFERENCES unidade_federacao(id)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""")
    con.commit()
    uf_id_map={}
    for sigla in sorted(df["sigla_uf"].unique()):
        cur.execute("INSERT INTO unidade_federacao(uf,descricao) VALUES(%s,%s)",(sigla,ESTADOS.get(sigla,sigla)))
        uf_id_map[sigla]=cur.lastrowid
    con.commit()
    print(f"{len(uf_id_map)} estados inseridos.")
    sql="INSERT INTO cidade(cod_munic,nome,uf_id,saude,educacao,renda,ifdm,ranking_nacional,ranking_estadual) VALUES(%s,%s,%s,%s,%s,%s,%s,%s,%s)"
    batch=[]
    for _,row in df.iterrows():
        batch.append((to_int(row["cod_munic"]),str(row["nome"]),uf_id_map[row["sigla_uf"]],
            to_float(row["saude"]),to_float(row["educacao"]),to_float(row["renda"]),
            to_float(row["ifdm"]),to_int(row["ranking_nacional"]),to_int(row["ranking_estadual"])))
        if len(batch)==500:
            cur.executemany(sql,batch); con.commit(); batch=[]
    if batch: cur.executemany(sql,batch); con.commit()
    print(f"{len(df)} municípios inseridos.")
    cur.execute("""UPDATE unidade_federacao u JOIN(
        SELECT uf_id,AVG(saude) ms,AVG(educacao) me,AVG(renda) mr,AVG(ifdm) mi
        FROM cidade GROUP BY uf_id) c ON u.id=c.uf_id
        SET u.media_saude=c.ms,u.media_educacao=c.me,u.media_renda=c.mr,u.media_ifdm=c.mi""")
    con.commit()
    cur.execute("SELECT COUNT(*) FROM cidade"); print(f"Verificacao: {cur.fetchone()[0]} cidades no banco.")
    cur.close(); con.close()
    print("Migracao concluida!")

if __name__=="__main__": main()
