package ifsp.edu.br.model.repostitory.implementation;

import ifsp.edu.br.model.dao.ClienteDao;
import ifsp.edu.br.model.database.ConexaoFactory;
import ifsp.edu.br.model.exception.EmailDuplicadoException;
import ifsp.edu.br.model.repostitory.ICadastrarUsuarioRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CadastrarUsuarioRepository implements ICadastrarUsuarioRepository {

    @Override
    public Integer cadastrarUsuario(ClienteDao clienteDao) throws EmailDuplicadoException {
        Connection conexao = ConexaoFactory.getConexao();

        try {
            if (buscarUsuarioByEmail(clienteDao.getEmail()) != null) {
                throw new EmailDuplicadoException("E-mail já cadastrado no sistema");
            }

            PreparedStatement preparedStatement = conexao.prepareStatement(
            "INSERT INTO cliente VALUES ('" + clienteDao.getId() + "', ?, ?, ?, ?, ?, ?, ?, ?);"
            );
            preparedStatement.setString(1, clienteDao.getNome());
            preparedStatement.setString(2, clienteDao.getEmail());
            preparedStatement.setString(3, clienteDao.getSenha());
            preparedStatement.setString(4, clienteDao.getCep());
            preparedStatement.setString(5, clienteDao.getLocalidade());
            preparedStatement.setString(6, clienteDao.getLogradouro());
            preparedStatement.setString(7, clienteDao.getBairro());
            preparedStatement.setString(8, clienteDao.getEstado());
            Integer statusQuery = preparedStatement.executeUpdate();

            preparedStatement.close();
            conexao.close();

            return statusQuery;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClienteDao buscarUsuarioByEmail(String email) {
        Connection conexao = ConexaoFactory.getConexao();

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conexao.prepareStatement("SELECT * FROM cliente WHERE email = ?");
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                return new ClienteDao(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("cep"),
                        rs.getString("cidade"),
                        rs.getString("logradouro"),
                        rs.getString("bairro"),
                        rs.getString("uf")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
