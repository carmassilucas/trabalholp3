package ifsp.edu.br.model;

import ifsp.edu.br.model.dao.implementation.MaterialDao;
import ifsp.edu.br.model.dto.CadastrarMaterialDto;
import ifsp.edu.br.model.exception.CadastrarMaterialException;
import ifsp.edu.br.model.vo.MaterialVo;

import java.util.List;

public class MaterialModelo {
    private static MaterialModelo instacia;
    private MaterialDao materialDao;
    private MaterialModelo() {
        materialDao = new MaterialDao();
    }

    public static MaterialModelo getInstacia() {
        if (instacia == null)
            instacia = new MaterialModelo();
        return instacia;
    }

    public List<MaterialVo> buscarMateriais() {
        return materialDao.buscarMateriais();
    }

    public void cadastrarMaterial(CadastrarMaterialDto cadastrarMaterialDto) throws CadastrarMaterialException {
        if (materialDao.buscarMaterialByNome(cadastrarMaterialDto.getNome()) != null)
            throw new CadastrarMaterialException("Material já cadastrado");
        materialDao.cadastrarMaterial(MaterialVo.toVo(cadastrarMaterialDto));
    }

    public void editarMaterial(MaterialVo material) {
        materialDao.editarMaterial(material);
    }
}
