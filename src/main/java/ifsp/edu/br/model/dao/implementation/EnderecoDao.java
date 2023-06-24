package ifsp.edu.br.model.dao.implementation;

import ifsp.edu.br.model.dao.IEnderecoDao;
import ifsp.edu.br.model.database.ConexaoFactory;
import ifsp.edu.br.model.vo.EnderecoVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnderecoDao implements IEnderecoDao {
    private static EnderecoDao instancia;

    private EnderecoDao() {

    }

    public static EnderecoDao getInstancia() {
        if (instancia == null)
            instancia = new EnderecoDao();
        return instancia;
    }

    @Override
    public EnderecoVo cadastrarEndereco(EnderecoVo endereco) {
        Connection conexao = ConexaoFactory.getConexao();

        EnderecoVo enderecoCadastrado = buscarEnderecoByCepAndNumero(endereco.getCep(), endereco.getNumero());

        if(enderecoCadastrado != null) {
            return enderecoCadastrado;
        }

        try {
            conexao.setAutoCommit(false);

            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "INSERT INTO endereco VALUES ('" + endereco.getId() + "', ?, ?, ?, ?, ?, ?);"
            );
            preparedStatement.setString(1, endereco.getCep());
            preparedStatement.setString(2, endereco.getCidade());
            preparedStatement.setString(3, endereco.getLogradouro());
            preparedStatement.setInt(4, endereco.getNumero());
            preparedStatement.setString(5, endereco.getBairro());
            preparedStatement.setString(6, endereco.getUf());
            preparedStatement.executeUpdate();

            conexao.commit();

            ConexaoFactory.fecharConexao(conexao, preparedStatement);

            return endereco;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EnderecoVo buscarEnderecoByCepAndNumero(String cep, Integer numero) {
        Connection conexao = ConexaoFactory.getConexao();

        PreparedStatement preparedStatement;
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
            ConexaoFactory.fecharConexao(conexao, preparedStatement, rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void relacionarClienteEndereco(UUID idCliente, UUID idEndereco) {
        Connection conexao = ConexaoFactory.getConexao();
        try {
            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "INSERT INTO cliente_endereco VALUES ('" + idCliente + "', '" + idEndereco + "');"
            );
            preparedStatement.executeUpdate();

            ConexaoFactory.fecharConexao(conexao, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EnderecoVo> getEnderecosByIdCliente(UUID id) {
        Connection conexao = ConexaoFactory.getConexao();
        PreparedStatement preparedStatement;
        try {
            List<EnderecoVo> enderecos = new ArrayList<>();

            preparedStatement = conexao.prepareStatement(
                    "SELECT e.* FROM cliente_endereco ce INNER JOIN endereco e ON ce.id_endereco = e.id " +
                            "WHERE ce.id_cliente = '" + id + "';"
            );
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                enderecos.add(new EnderecoVo(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("cep"),
                        rs.getString("cidade"),
                        rs.getString("logradouro"),
                        rs.getString("bairro"),
                        rs.getString("uf"),
                        rs.getInt("numero"))
                );
            }
            ConexaoFactory.fecharConexao(conexao, preparedStatement, rs);
            return enderecos;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
