package ifdm.dao;
import ifdm.model.Cidade;
import java.sql.*; import java.util.*;
public class CidadeDAO {
    public List<Cidade> listarPorUf(int ufId) throws SQLException {
        List<Cidade> lista=new ArrayList<>();
        String sql="SELECT c.id,c.cod_munic,c.nome,c.uf_id,u.uf AS sigla_uf,c.saude,c.educacao,c.renda,c.ifdm,c.ranking_nacional,c.ranking_estadual FROM cidade c JOIN unidade_federacao u ON u.id=c.uf_id WHERE c.uf_id=? ORDER BY c.nome";
        try(Connection c=Conexao.getConexao(); PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,ufId);
            try(ResultSet r=p.executeQuery()){ while(r.next()) lista.add(mapear(r)); }
        }
        return lista;
    }
    public Cidade buscarPorId(int id) throws SQLException {
        String sql="SELECT c.id,c.cod_munic,c.nome,c.uf_id,u.uf AS sigla_uf,c.saude,c.educacao,c.renda,c.ifdm,c.ranking_nacional,c.ranking_estadual FROM cidade c JOIN unidade_federacao u ON u.id=c.uf_id WHERE c.id=?";
        try(Connection c=Conexao.getConexao(); PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,id);
            try(ResultSet r=p.executeQuery()){ if(r.next()) return mapear(r); }
        }
        return null;
    }
    private Cidade mapear(ResultSet r) throws SQLException {
        Cidade c=new Cidade();
        c.setId(r.getInt("id")); c.setCodMunic(r.getInt("cod_munic")); c.setNome(r.getString("nome"));
        c.setUfId(r.getInt("uf_id")); c.setSiglaUf(r.getString("sigla_uf"));
        c.setSaude(r.getDouble("saude")); c.setEducacao(r.getDouble("educacao"));
        c.setRenda(r.getDouble("renda")); c.setIfdm(r.getDouble("ifdm"));
        c.setRankingNacional(r.getInt("ranking_nacional")); c.setRankingEstadual(r.getInt("ranking_estadual"));
        return c;
    }
}
