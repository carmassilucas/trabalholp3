package ifsp.edu.br.model.dao.implementation;

import ifsp.edu.br.model.dao.IReciclagemDao;
import ifsp.edu.br.model.database.ConexaoFactory;
import ifsp.edu.br.model.dto.LoginReciclagemDto;
import ifsp.edu.br.model.dto.RelacionarMaterialReciclagemDto;
import ifsp.edu.br.model.util.MessageDigestUtils;
import ifsp.edu.br.model.vo.EnderecoVo;
import ifsp.edu.br.model.vo.MaterialReciclagemVo;
import ifsp.edu.br.model.vo.ReciclagemVo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
    public ReciclagemVo buscarReciclagemByUsuarioAndSenha(LoginReciclagemDto dto) {
        Connection conexao = ConexaoFactory.getConexao();
        PreparedStatement preparedStatement;
        try {
            ReciclagemVo reciclagemVo = null;

            preparedStatement = conexao.prepareStatement("SELECT * FROM reciclagem WHERE usuario = ? AND senha = ? LIMIT 1;");
            preparedStatement.setString(1, dto.getUsuario());
            preparedStatement.setString(2, MessageDigestUtils.hashSenha(dto.getSenha()));
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
    public Integer relacionarMaterialReciclagem(RelacionarMaterialReciclagemDto dto) {
        Connection conexao = ConexaoFactory.getConexao();
        if (verificarRelacionamentoMaterialReciclagem(dto))
            return 0;

        try {
            conexao.setAutoCommit(false);

            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "INSERT INTO material_reciclagem VALUES ('" + dto.getIdMaterial() + "', '" + dto.getIdReciclagem() + "', ?);"
            );
            if (dto.getPreco() == null)
                preparedStatement.setNull(1, Types.FLOAT);
            else
                preparedStatement.setFloat(1, dto.getPreco());
            preparedStatement.executeUpdate();

            conexao.commit();

            ConexaoFactory.fecharConexao(conexao, preparedStatement);

            return 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cadastrarReciclagem(ReciclagemVo reciclagemVo) {
        Connection conexao = ConexaoFactory.getConexao();
        try {
            conexao.setAutoCommit(false);

            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "INSERT INTO reciclagem VALUES ('" + reciclagemVo.getId() + "', '" + reciclagemVo.getEndereco().getId() + "', ?, ?, ?);"
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

    public boolean verificarRelacionamentoMaterialReciclagem(RelacionarMaterialReciclagemDto dto) {
        Connection conexao = ConexaoFactory.getConexao();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = conexao.prepareStatement(
                    "SELECT * FROM material_reciclagem WHERE id_material = '" + dto.getIdMaterial().toString() +
                            "' AND id_reciclagem = '" + dto.getIdReciclagem().toString() + "' LIMIT 1;"
            );

            ResultSet rs = preparedStatement.executeQuery();
            boolean existeRelacionamento = rs.next();

            ConexaoFactory.fecharConexao(conexao, preparedStatement, rs);
            return existeRelacionamento;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MaterialReciclagemVo> buscarMateriais(UUID idReciclagem) {
        Connection conexao = ConexaoFactory.getConexao();
        List<MaterialReciclagemVo> materiais = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "SELECT mr.* FROM material AS m INNER JOIN material_reciclagem mr ON mr.id_material = m.id " +
                            "WHERE mr.id_reciclagem = '" + idReciclagem + "';"
            );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                materiais.add(new MaterialReciclagemVo(
                        UUID.fromString(rs.getString("id_material")),
                        UUID.fromString(rs.getString("id_reciclagem")),
                        rs.getFloat("preco")
                ));
            }
            ConexaoFactory.fecharConexao(conexao, preparedStatement, rs);
            return materiais;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void editarPrecoMaterial(RelacionarMaterialReciclagemDto dto) {
        Connection conexao = ConexaoFactory.getConexao();
        try {
            conexao.setAutoCommit(false);

            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "UPDATE material_reciclagem SET preco = ? WHERE id_material = '" + dto.getIdMaterial() + "' AND " +
                            "id_reciclagem = '" + dto.getIdReciclagem() + "';"
            );
            if (dto.getPreco() == null)
                preparedStatement.setNull(1, Types.FLOAT);
            else
                preparedStatement.setFloat(1, dto.getPreco());
            preparedStatement.executeUpdate();

            conexao.commit();

            ConexaoFactory.fecharConexao(conexao, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
