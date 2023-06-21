package ifsp.edu.br.model.dao;

import ifsp.edu.br.model.exception.MaterialDuplicadoException;
import ifsp.edu.br.model.vo.MaterialVo;

import java.util.List;

public interface IMaterialDao {
    void cadastrarMaterial(MaterialVo material) throws MaterialDuplicadoException;
    MaterialVo buscarMaterialByNome(String nome);
    List<MaterialVo> buscarMateriais();
    void editarMaterial(MaterialVo material);
}
