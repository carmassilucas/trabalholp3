package ifsp.edu.br.model;

import ifsp.edu.br.model.dao.implementation.MaterialDao;
import ifsp.edu.br.model.dto.CadastrarMaterialDto;
import ifsp.edu.br.model.exception.MaterialDuplicadoException;
import ifsp.edu.br.model.vo.MaterialVo;

import java.util.List;

public class MaterialModelo {
    private static MaterialModelo instacia;
    private final MaterialDao materialDao;
    private MaterialModelo() {
        materialDao = MaterialDao.getInstancia();
    }

    public static MaterialModelo getInstacia() {
        if (instacia == null)
            instacia = new MaterialModelo();
        return instacia;
    }

    public List<MaterialVo> buscarMateriais() {
        return materialDao.buscarMateriais();
    }

    public void cadastrarMaterial(CadastrarMaterialDto cadastrarMaterialDto) throws MaterialDuplicadoException {
        if (materialDao.buscarMaterialByNome(cadastrarMaterialDto.getNome()) != null)
            throw new MaterialDuplicadoException("Material já cadastrado");
        materialDao.cadastrarMaterial(MaterialVo.toVo(cadastrarMaterialDto));
    }

    public void editarMaterial(MaterialVo material) {
        materialDao.editarMaterial(material);
    }
}
