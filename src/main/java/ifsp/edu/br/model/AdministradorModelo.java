package ifsp.edu.br.model;

import ifsp.edu.br.model.dao.implementation.AdministradorDao;
import ifsp.edu.br.model.dto.AutenticacaoDto;
import ifsp.edu.br.model.vo.AdministradorVo;

public class AdministradorModelo {
    private static AdministradorModelo instancia;
    private final AdministradorDao administradorDao;

    private AdministradorModelo() {
        administradorDao = AdministradorDao.getInstancia();
    }

    public static AdministradorModelo getInstancia() {
        if (instancia == null)
            instancia = new AdministradorModelo();
        return instancia;
    }

    public AdministradorVo autenticar(AutenticacaoDto dto) {
        return administradorDao.getByUsuarioAndSenha(dto);
    }
}
