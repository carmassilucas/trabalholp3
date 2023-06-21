package ifsp.edu.br.control;

import ifsp.edu.br.control.exception.CadastrarMaterialException;
import ifsp.edu.br.model.MaterialModelo;
import ifsp.edu.br.model.dto.CadastrarMaterialDto;
import ifsp.edu.br.model.exception.MaterialDuplicadoException;
import ifsp.edu.br.model.util.DtoUtils;
import ifsp.edu.br.model.vo.MaterialVo;

import java.util.List;

public class MaterialControle {
    private static MaterialControle instancia;
    private final MaterialModelo materialModelo;

    private MaterialControle() {
        materialModelo = MaterialModelo.getInstacia();
    }

    public static MaterialControle getInstancia() {
        if (instancia == null)
            instancia = new MaterialControle();
        return instancia;
    }

    public List<MaterialVo> buscarMateriais() {
        return materialModelo.buscarMateriais();
    }

    public void cadastrarMaterial(CadastrarMaterialDto dto) throws CadastrarMaterialException,
            MaterialDuplicadoException {
        if (DtoUtils.verificaSeAtributoNullOrEmpty(dto))
            throw new CadastrarMaterialException("Preencha todos os campos para cadastrar o material");
        materialModelo.cadastrarMaterial(dto);
    }

    public void editarMaterial(MaterialVo material) {
        materialModelo.editarMaterial(material);
    }
}
