package ifsp.edu.br.model.dao;

import ifsp.edu.br.model.dto.AutenticacaoDto;
import ifsp.edu.br.model.vo.AdministradorVo;

public interface IAdministradorDao {
    AdministradorVo getByUsuarioAndSenha(AutenticacaoDto dto);
}
