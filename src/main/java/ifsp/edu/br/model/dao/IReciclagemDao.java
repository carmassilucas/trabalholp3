package ifsp.edu.br.model.dao;

import ifsp.edu.br.model.dto.LoginReciclagemDto;
import ifsp.edu.br.model.dto.RelacionarMaterialReciclagemDto;
import ifsp.edu.br.model.vo.MaterialReciclagemVo;
import ifsp.edu.br.model.vo.ReciclagemVo;

import java.util.List;
import java.util.UUID;

public interface IReciclagemDao {
    void cadastrarReciclagem(ReciclagemVo reciclagemVo);
    ReciclagemVo buscarReciclagemByUsuario(String usuario);

    ReciclagemVo buscarReciclagemByUsuarioAndSenha(LoginReciclagemDto dto);

    Integer relacionarMaterialReciclagem(RelacionarMaterialReciclagemDto dto);

    boolean verificarRelacionamentoMaterialReciclagem(RelacionarMaterialReciclagemDto dto);

    List<MaterialReciclagemVo> buscarMateriais(UUID idReciclagem);

    void editarPrecoMaterial(RelacionarMaterialReciclagemDto dto);
}
