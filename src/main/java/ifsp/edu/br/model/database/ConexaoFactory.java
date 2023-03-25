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

    public static void closeConexao(Connection conexao) {
        try {
            if (conexao != null) {
                conexao.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao tentar fechar a conexão: ", e);
        }
    }
}
