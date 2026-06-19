package ifdm.dao;

import ifdm.model.UnidadeFederacao;

import java.sql.*; import java.util.*;

public class UnidadeFederacaoDAO {
    public List<UnidadeFederacao> listarTodos() throws SQLException {
        List<UnidadeFederacao> lista = new ArrayList<>();
        String sql = "SELECT id,uf,descricao,media_saude,media_educacao,media_renda,media_ifdm FROM unidade_federacao ORDER BY descricao";
        try(Connection c=Conexao.getConexao(); Statement s=c.createStatement(); ResultSet r=s.executeQuery(sql)){
            while(r.next()) lista.add(mapear(r));
        }
        return lista;
    }
    public UnidadeFederacao buscarPorId(int id) throws SQLException {
        String sql = "SELECT id,uf,descricao,media_saude,media_educacao,media_renda,media_ifdm FROM unidade_federacao WHERE id=?";
        try(Connection c=Conexao.getConexao(); PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,id);
            try(ResultSet r=p.executeQuery()){ if(r.next()) return mapear(r); }
        }
        return null;
    }
    private UnidadeFederacao mapear(ResultSet r) throws SQLException {
        UnidadeFederacao u=new UnidadeFederacao();
        u.setId(r.getInt("id")); u.setUf(r.getString("uf")); u.setDescricao(r.getString("descricao"));
        u.setMediaSaude(r.getDouble("media_saude")); u.setMediaEducacao(r.getDouble("media_educacao"));
        u.setMediaRenda(r.getDouble("media_renda")); u.setMediaIfdm(r.getDouble("media_ifdm"));
        return u;
    }
}
