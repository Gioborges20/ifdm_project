# IFDM — Como rodar o projeto

## Pré-requisitos
- Codeanywhere com container Ubuntu
- Planilha `Serie-Historica-IFDM-2013-a-2023.xlsx` na pasta `python_migration/`

---

## 1. Instalar e ligar o MySQL

bash
sudo apt-get update -qq
sudo apt-get install -y mysql-server
sudo service mysql start
sudo mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root'; FLUSH PRIVILEGES;"


---

## 2. Migrar a planilha para o banco

bash
cd ifdm_project/python_migration
pip install pandas openpyxl mysql-connector-python
python3 migrar_para_mysql.py


---

## 2. Compilar e rodar o Java

bash
cd ../java_project
chmod +x compilar_e_rodar.sh
./compilar_e_rodar.sh


O script baixa o driver MySQL automaticamente e abre a janela do programa.

---

## Toda vez que reabrir o Codeanywhere

O banco de dados apaga quando o container reinicia. Rode isso antes de tudo:

bash
sudo service mysql start


## Observação

Olá, Professor! Fiz esse trabalho sem meu computador pessoal, onde não consegui baixar IDE nem o MySQL Workbench, então utilizei essa IDE online e rodei o banco no terminal. Porém, no caso de ronar no neatbens ou intelij é o mesmo processo que fazemos em aula. O User e Path segue o padrão root.
Obrigada e desculpa o formato!
