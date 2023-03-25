package ifsp.edu.br.model.repostitory.implementation;

import ifsp.edu.br.model.dao.ClienteDao;
import ifsp.edu.br.model.database.ConexaoFactory;
import ifsp.edu.br.model.repostitory.ICadastrarUsuarioRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CadastrarUsuarioRepository implements ICadastrarUsuarioRepository {

    @Override
    public Integer cadastrarUsuario(ClienteDao clienteDao) {
        Connection conexao = ConexaoFactory.getConexao();

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conexao.prepareStatement(
            "INSERT INTO cliente VALUES ('" + clienteDao.getId() + "', ?, ?, ?, ?, ?, ?, ?);"
            );
            preparedStatement.setString(1, clienteDao.getNome());
            preparedStatement.setString(2, clienteDao.getEmail());
            preparedStatement.setString(3, clienteDao.getCep());
            preparedStatement.setString(4, clienteDao.getLocalidade());
            preparedStatement.setString(5, clienteDao.getLogradouro());
            preparedStatement.setString(6, clienteDao.getBairro());
            preparedStatement.setString(7, clienteDao.getEstado());
            Integer statusQuery = preparedStatement.executeUpdate();

            preparedStatement.close();
            conexao.close();

            return statusQuery;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
