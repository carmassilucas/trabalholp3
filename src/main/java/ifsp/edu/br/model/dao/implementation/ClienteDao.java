package ifsp.edu.br.model.dao.implementation;

import ifsp.edu.br.model.dao.IClienteDao;
import ifsp.edu.br.model.database.ConexaoFactory;
import ifsp.edu.br.model.dto.AutenticacaoDto;
import ifsp.edu.br.model.util.MessageDigestUtils;
import ifsp.edu.br.model.vo.ClienteVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ClienteDao implements IClienteDao {
    private static ClienteDao instancia;

    private ClienteDao() {

    }

    public static ClienteDao getInstancia() {
        if (instancia == null)
            instancia = new ClienteDao();
        return instancia;
    }

    @Override
    public ClienteVo cadastrarUsuario(ClienteVo cliente) {
        Connection conexao = ConexaoFactory.getConexao();

        try {
            PreparedStatement preparedStatement = conexao.prepareStatement(
            "INSERT INTO cliente VALUES ('" + cliente.getId() + "', ?, ?, ?);"
            );
            preparedStatement.setString(1, cliente.getNome());
            preparedStatement.setString(2, cliente.getEmail());
            preparedStatement.setString(3, cliente.getSenha());
            preparedStatement.executeUpdate();

            ConexaoFactory.fecharConexao(conexao, preparedStatement);
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
            ClienteVo c = null;
            preparedStatement = conexao.prepareStatement("SELECT * FROM cliente WHERE email = ? LIMIT 1;");
            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                 c = new ClienteVo(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        null
                );
                 break;
            }
            ConexaoFactory.fecharConexao(conexao, preparedStatement, rs);
            return c;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClienteVo buscarClienteByEmailAndSenha(AutenticacaoDto dto) {
        Connection conexao = ConexaoFactory.getConexao();
        PreparedStatement preparedStatement;
        try {
            ClienteVo clienteVo = null;

            preparedStatement = conexao.prepareStatement("SELECT * FROM cliente WHERE email = ? AND senha = ? LIMIT 1;");
            preparedStatement.setString(1, dto.getUsuario());
            preparedStatement.setString(2, MessageDigestUtils.hashSenha(dto.getSenha()));
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                clienteVo = new ClienteVo(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        null
                );
                break;
            }
            ConexaoFactory.fecharConexao(conexao, preparedStatement, rs);
            return clienteVo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
