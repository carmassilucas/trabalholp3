package ifsp.edu.br.model.dao;

import ifsp.edu.br.model.exception.CadastrarMaterialException;
import ifsp.edu.br.model.vo.MaterialVo;

import java.util.List;

public interface IMaterialDao {
    MaterialVo cadastrarMaterial(MaterialVo material) throws CadastrarMaterialException;
    MaterialVo buscarMaterialByNome(String nome);
    List<MaterialVo> buscarMateriais();
    void editarMaterial(MaterialVo material);
}
