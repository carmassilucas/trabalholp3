package ifsp.edu.br.model.dao.implementation;

import ifsp.edu.br.model.dao.IReciclagemDao;
import ifsp.edu.br.model.database.ConexaoFactory;
import ifsp.edu.br.model.vo.EnderecoVo;
import ifsp.edu.br.model.vo.ReciclagemVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ReciclagemDao implements IReciclagemDao {
    private static ReciclagemDao instancia;

    private ReciclagemDao() {

    }

    public static ReciclagemDao getInstancia() {
        if (instancia == null)
            instancia = new ReciclagemDao();
        return instancia;
    }

    @Override
    public ReciclagemVo buscarReciclagemByUsuario(String usuario) {
        Connection conexao = ConexaoFactory.getConexao();

        PreparedStatement preparedStatement;
        try {
            ReciclagemVo reciclagemVo = null;

            preparedStatement = conexao.prepareStatement("SELECT * FROM reciclagem WHERE usuario = ? LIMIT 1;");
            preparedStatement.setString(1, usuario);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                reciclagemVo = new ReciclagemVo(
                        UUID.fromString(rs.getString("id")),
                        new EnderecoVo(UUID.fromString(rs.getString("id_endereco"))),
                        rs.getString("nome"),
                        rs.getString("usuario"),
                        rs.getString("senha")
                );
                break;
            }
            ConexaoFactory.fecharConexao(conexao, preparedStatement, rs);
            return reciclagemVo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cadastrarReciclagem(ReciclagemVo reciclagemVo) {
        Connection conexao = ConexaoFactory.getConexao();

        try {
            conexao.setAutoCommit(false);

            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "INSERT INTO reciclagem VALUES ('" + reciclagemVo.getId() + "', '" +reciclagemVo.getEndereco().getId() + "', ?, ?, ?);"
            );
            preparedStatement.setString(1, reciclagemVo.getNome());
            preparedStatement.setString(2, reciclagemVo.getUsuario());
            preparedStatement.setString(3, reciclagemVo.getSenha());
            preparedStatement.executeUpdate();

            conexao.commit();

            ConexaoFactory.fecharConexao(conexao, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
