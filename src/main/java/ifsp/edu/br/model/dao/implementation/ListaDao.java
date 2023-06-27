package ifsp.edu.br.model.dao.implementation;

import ifsp.edu.br.model.dao.IListaDao;
import ifsp.edu.br.model.database.ConexaoFactory;
import ifsp.edu.br.model.dto.BuscarMateriaisListaDto;
import ifsp.edu.br.model.dto.RemoverMaterialListaDto;
import ifsp.edu.br.model.vo.ListaVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListaDao implements IListaDao {

    private static ListaDao instancia;

    private ListaDao() {

    }

    public static ListaDao getInstancia() {
        if (instancia == null)
            instancia = new ListaDao();
        return instancia;
    }

    @Override
    public void cadastrarMaterialLista(ListaVo lista) {
        Connection conexao = ConexaoFactory.getConexao();
        try {
            conexao.setAutoCommit(false);

            if (verificaIfCLienteMaterialExists(lista)) {
                PreparedStatement preparedStatement = conexao.prepareStatement(
                        "UPDATE lista SET id_reciclagem = '" + lista.getIdReciclagem() + "' " +
                                "WHERE id_cliente = '" + lista.getIdCliente() + "' AND " +
                                "id_material = '" + lista.getIdMaterial() + "';"
                );
                preparedStatement.executeUpdate();
                conexao.commit();

                ConexaoFactory.fecharConexao(conexao, preparedStatement);
                return;
            }

            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "INSERT INTO lista VALUES ('" + lista.getIdCliente() + "', '" +
                            lista.getIdMaterial() + "', '" + lista.getIdReciclagem() + "');"
            );
            preparedStatement.executeUpdate();
            conexao.commit();

            ConexaoFactory.fecharConexao(conexao, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean verificaIfCLienteMaterialExists(ListaVo lista) {
        Connection conexao = ConexaoFactory.getConexao();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = conexao.prepareStatement(
                    "SELECT * FROM lista WHERE id_cliente = '" + lista.getIdCliente() +
                            "' AND id_material = '" + lista.getIdMaterial() + "' LIMIT 1;"
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
    public List<BuscarMateriaisListaDto> getListaMateriais(UUID idCliente) {
        Connection conexao = ConexaoFactory.getConexao();
        List<BuscarMateriaisListaDto> listaMateriais = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "SELECT m.nome nome_material, r.nome nome_reciclagem, e.logradouro endereco_reciclagem, e.numero, mr.preco " +
                            "FROM lista l " +
                            "INNER JOIN material_reciclagem mr ON mr.id_material = l.id_material " +
                            "AND mr.id_reciclagem = l.id_reciclagem\n" +
                            "INNER JOIN material m ON mr.id_material = m.id\n" +
                            "INNER JOIN reciclagem r ON mr.id_reciclagem = r.id \n" +
                            "INNER JOIN endereco e ON e.id = r.id_endereco\n" +
                            "WHERE l.id_cliente = '" + idCliente + "';"
            );
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                listaMateriais.add(new BuscarMateriaisListaDto(
                        rs.getString("nome_material"),
                        rs.getString("nome_reciclagem"),
                        rs.getString("endereco_reciclagem"),
                        rs.getInt("numero"),
                        rs.getFloat("preco")
                ));
            }
            ConexaoFactory.fecharConexao(conexao, preparedStatement, rs);
            return listaMateriais;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removerMaterialLista(RemoverMaterialListaDto dto) {
        Connection conexao = ConexaoFactory.getConexao();
        try {
            conexao.setAutoCommit(false);
            PreparedStatement preparedStatement = conexao.prepareStatement(
                    "DELETE FROM lista WHERE id_cliente = '" + dto.getIdCliente() + "' " +
                            "AND id_material = '" + dto.getIdMaterial() + "';"
            );
            preparedStatement.executeUpdate();
            conexao.commit();

            ConexaoFactory.fecharConexao(conexao, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
