package ifdm.dao;
import java.sql.*;
public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/ifdm?useSSL=false&serverTimezone=America/Sao_Paulo&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASS = "root";
    private static Connection instancia;
    private Conexao() {}
    public static Connection getConexao() throws SQLException {
        if (instancia == null || instancia.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instancia = DriverManager.getConnection(URL, USER, PASS);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL nao encontrado. Adicione mysql-connector-j.jar em lib/", e);
            }
        }
        return instancia;
    }
    public static void fechar() {
        try { if (instancia != null && !instancia.isClosed()) instancia.close(); }
        catch (SQLException e) { System.err.println("Erro ao fechar: " + e.getMessage()); }
    }
}
