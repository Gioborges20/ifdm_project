package ifdm.controller;
import ifdm.dao.*; import ifdm.model.*;
import java.sql.SQLException; import java.util.*;
public class IFDMController {
    private final UnidadeFederacaoDAO ufDAO = new UnidadeFederacaoDAO();
    private final CidadeDAO cidadeDAO = new CidadeDAO();
    public List<UnidadeFederacao> getTodasUFs() {
        try { List<UnidadeFederacao> l=ufDAO.listarTodos(); Collections.sort(l); return l; }
        catch(SQLException e){ throw new RuntimeException("Erro ao listar estados: "+e.getMessage(),e); }
    }
    public UnidadeFederacao getUFporId(int id) {
        try { return ufDAO.buscarPorId(id); }
        catch(SQLException e){ throw new RuntimeException("Erro ao buscar estado: "+e.getMessage(),e); }
    }
    public List<Cidade> getCidadesPorUF(int ufId) {
        try { List<Cidade> l=cidadeDAO.listarPorUf(ufId); Collections.sort(l); return l; }
        catch(SQLException e){ throw new RuntimeException("Erro ao listar cidades: "+e.getMessage(),e); }
    }
    public Cidade getCidadePorId(int id) {
        try { return cidadeDAO.buscarPorId(id); }
        catch(SQLException e){ throw new RuntimeException("Erro ao buscar cidade: "+e.getMessage(),e); }
    }
}
