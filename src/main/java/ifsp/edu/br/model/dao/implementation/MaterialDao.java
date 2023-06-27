package ifsp.edu.br.model.dao.implementation;

import ifsp.edu.br.model.dao.IMaterialDao;
import ifsp.edu.br.model.database.ConexaoFactory;
import ifsp.edu.br.model.vo.MaterialVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaterialDao implements IMaterialDao {
    private static MaterialDao instancia;

    private MaterialDao() {

    }

    public static MaterialDao getInstancia() {
        if (instancia == null)
            instancia = new MaterialDao();
        return instancia;
    }

    @Override
    public void cadastrarMaterial(MaterialVo material) {
        Connection conexao = ConexaoFactory.getConexao();
        try {
            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "INSERT INTO material VALUES ('" + material.getId() + "', ?, ?);"
            );
            preparedStatement.setString(1, material.getNome());
            preparedStatement.setString(2, material.getDescricao());
            preparedStatement.executeUpdate();

            ConexaoFactory.fecharConexao(conexao, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MaterialVo buscarMaterialByNome(String nome) {
        Connection conexao = ConexaoFactory.getConexao();
        try {
            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "SELECT * FROM material WHERE nome ILIKE ? LIMIT 1;"
            );
            preparedStatement.setString(1, nome);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                MaterialVo material = new MaterialVo(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("nome"),
                        rs.getString("descricao")
                );
                ConexaoFactory.fecharConexao(conexao, preparedStatement, rs);
                return material;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<MaterialVo> buscarMateriais() {
        Connection conexao = ConexaoFactory.getConexao();
        List<MaterialVo> materiais = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "SELECT * FROM material;"
            );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                materiais.add(new MaterialVo(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("nome"),
                        rs.getString("descricao")
                ));
            }
            ConexaoFactory.fecharConexao(conexao, preparedStatement, rs);
            return materiais;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void editarMaterial(MaterialVo material) {
        Connection conexao = ConexaoFactory.getConexao();
        try {
            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "UPDATE material SET nome = ?, descricao = ? WHERE id = '" + material.getId() + "';"
            );
            preparedStatement.setString(1, material.getNome());
            preparedStatement.setString(2, material.getDescricao());
            preparedStatement.executeUpdate();

            ConexaoFactory.fecharConexao(conexao, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MaterialVo getById(UUID id) {
        Connection conexao = ConexaoFactory.getConexao();
        try {
            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "SELECT * FROM material WHERE id = '" + id + "' LIMIT 1;"
            );
            ResultSet rs = preparedStatement.executeQuery();

            MaterialVo material = null;
            while (rs.next()) {
                material = new MaterialVo(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("nome"),
                        rs.getString("descricao")
                );
            }
            ConexaoFactory.fecharConexao(conexao, preparedStatement, rs);
            return material;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
