package ifsp.edu.br.model.dao.implementation;

import ifsp.edu.br.model.dao.IEnderecoDao;
import ifsp.edu.br.model.database.ConexaoFactory;
import ifsp.edu.br.model.exception.CadastrarEnderecoException;
import ifsp.edu.br.model.vo.EnderecoVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EnderecoDao implements IEnderecoDao {
    @Override
    public void cadastrarEndereco(EnderecoVo endereco, UUID idCliente) throws CadastrarEnderecoException {
        Connection conexao = ConexaoFactory.getConexao();

        EnderecoVo enderecoCadastrado = buscarEnderecoByCepAndNumero(endereco.getCep(), endereco.getNumero());

        if(enderecoCadastrado != null) {
            relacionarClienteEndereco(idCliente, enderecoCadastrado.getId());
            return;
        }

        try {
            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "INSERT INTO endereco VALUES ('" + endereco.getId() + "', ?, ?, ?, ?, ?, ?);"
            );
            preparedStatement.setString(1, endereco.getCep());
            preparedStatement.setString(2, endereco.getCidade());
            preparedStatement.setString(3, endereco.getLogradouro());
            preparedStatement.setInt(4, endereco.getNumero());
            preparedStatement.setString(5, endereco.getBairro());
            preparedStatement.setString(6, endereco.getUf());
            var statusQuery = preparedStatement.executeUpdate();

            if (statusQuery != 1) {
                throw new CadastrarEnderecoException("Erro inesperado ao cadastrar endereco");
            }

            ConexaoFactory.closeConnection(conexao, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        relacionarClienteEndereco(idCliente, endereco.getId());
    }

    @Override
    public EnderecoVo buscarEnderecoByCepAndNumero(String cep, Integer numero) {
        Connection conexao = ConexaoFactory.getConexao();

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conexao.prepareStatement("SELECT * FROM endereco WHERE cep = ? AND numero = ? LIMIT 1;");
            preparedStatement.setString(1, cep);
            preparedStatement.setInt(2, numero);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                return new EnderecoVo(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("cep"),
                        rs.getString("cidade"),
                        rs.getString("logradouro"),
                        rs.getString("bairro"),
                        rs.getString("uf"),
                        rs.getInt("numero")

                );
            }
            ConexaoFactory.closeConnection(conexao, preparedStatement, rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void relacionarClienteEndereco(UUID idCliente, UUID idEndereco) throws CadastrarEnderecoException {
        Connection conexao = ConexaoFactory.getConexao();

        try {
            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "INSERT INTO cliente_endereco VALUES ('" + idCliente + "', '" + idEndereco + "');"
            );
            var statusQuery = preparedStatement.executeUpdate();

            if (statusQuery != 1) {
                throw new CadastrarEnderecoException("Erro inesperado ao relacionar cliente e endereço.");
            }

            ConexaoFactory.closeConnection(conexao, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
