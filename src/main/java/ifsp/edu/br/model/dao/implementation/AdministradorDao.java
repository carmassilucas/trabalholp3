package ifsp.edu.br.model.dao.implementation;

import ifsp.edu.br.model.dao.IAdministradorDao;
import ifsp.edu.br.model.database.ConexaoFactory;
import ifsp.edu.br.model.dto.AutenticacaoDto;
import ifsp.edu.br.model.util.MessageDigestUtils;
import ifsp.edu.br.model.vo.AdministradorVo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class AdministradorDao implements IAdministradorDao {
    private static AdministradorDao instancia;

    private AdministradorDao() {

    }

    public static AdministradorDao getInstancia() {
        if (instancia == null)
            instancia = new AdministradorDao();
        return instancia;
    }

    @Override
    public AdministradorVo getByUsuarioAndSenha(AutenticacaoDto dto) {
        Connection conexao = ConexaoFactory.getConexao();
        PreparedStatement preparedStatement;

        try {
            AdministradorVo administrador = null;

            preparedStatement = conexao.prepareStatement("SELECT * FROM administrador WHERE usuario = ? AND senha = ? LIMIT 1;");
            preparedStatement.setString(1, dto.getUsuario());
            preparedStatement.setString(2, MessageDigestUtils.hashSenha(dto.getSenha()));
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                administrador = new AdministradorVo(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("usuario"),
                        rs.getString("senha")
                );
                break;
            }

            ConexaoFactory.fecharConexao(conexao, preparedStatement, rs);
            return administrador;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
