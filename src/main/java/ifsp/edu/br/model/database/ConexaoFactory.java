package ifsp.edu.br.model.database;

import java.sql.*;

public class ConexaoFactory {

    // TODO verificar multiplos parâmetros no método getConnection do DriverManager
    private static final String url = "jdbc:postgres://localhost/reciclagem";
    private static final String usuario = "dockerpostgres";
    private static final String senha = "postgrespass";

    public static Connection getConexao() {
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost/reciclagem?user=dockerpostgres&password=postgrespass");
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
