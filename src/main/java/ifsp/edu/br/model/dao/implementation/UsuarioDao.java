package ifsp.edu.br.model.dao.implementation;

import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.dao.IUsuarioDao;
import ifsp.edu.br.model.database.ConexaoFactory;
import ifsp.edu.br.model.vo.ClienteVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UsuarioDao implements IUsuarioDao {

    @Override
    public ClienteVo cadastrarUsuario(ClienteVo cliente) throws CadastrarClienteException {
        Connection conexao = ConexaoFactory.getConexao();

        try {
            PreparedStatement preparedStatement = conexao.prepareStatement(
            "INSERT INTO cliente VALUES ('" + cliente.getId() + "', ?, ?, ?);"
            );
            preparedStatement.setString(1, cliente.getNome());
            preparedStatement.setString(2, cliente.getEmail());
            preparedStatement.setString(3, cliente.getSenha());
            var statusQuery = preparedStatement.executeUpdate();

            if(statusQuery != 1) {
                throw new CadastrarClienteException("Erro inesperado ao cadastrar cliente");
            }

            preparedStatement.close();
            conexao.close();

            return cliente;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClienteVo buscarUsuarioByEmail(String email) {
        Connection conexao = ConexaoFactory.getConexao();

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conexao.prepareStatement("SELECT * FROM cliente WHERE email = ? LIMIT 1;");
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                return new ClienteVo(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        null
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
