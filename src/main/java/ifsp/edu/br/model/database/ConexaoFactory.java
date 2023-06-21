package ifsp.edu.br.model.database;

import java.sql.*;

public class ConexaoFactory {
    private static final String url = "jdbc:postgresql://localhost/reciclagem";
    private static final String usuario = "dockerpostgres";
    private static final String senha = "postgrespass";

    public static Connection getConexao() {
        try {
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao tentar se conectar ao banco de dados: ", e);
        }
    }

    public static void fecharConexao(Connection conexao) {
        try {
            if (conexao != null)
                conexao.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão com banco de dados: " + e);
        }
    }

    public static void fecharConexao(Connection conexao, PreparedStatement stmt) {
        fecharConexao(conexao);
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão com banco de dados: " + e);
        }
    }

    public static void fecharConexao(Connection conexao, PreparedStatement stmt, ResultSet rs) {
        fecharConexao(conexao, stmt);
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão com banco de dados: " + e);
        }
    }
}
