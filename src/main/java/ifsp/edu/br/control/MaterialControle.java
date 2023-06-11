package ifsp.edu.br.control;

import ifsp.edu.br.control.exception.CadastrarMaterialException;
import ifsp.edu.br.model.MaterialModelo;
import ifsp.edu.br.model.dto.CadastrarMaterialDto;
import ifsp.edu.br.model.vo.MaterialVo;

import java.util.List;

public class MaterialControle {
    private static MaterialControle instancia;
    private MaterialModelo materialModelo;
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

    public void cadastrarMaterial(CadastrarMaterialDto cadastrarMaterialDto) throws CadastrarMaterialException,
            ifsp.edu.br.model.exception.CadastrarMaterialException {
        if (cadastrarMaterialDto.verificaCampos())
            throw new CadastrarMaterialException("Preencha todos os campos para cadastrar o material");
        materialModelo.cadastrarMaterial(cadastrarMaterialDto);
    }

    public void editarMaterial(MaterialVo material) {
        materialModelo.editarMaterial(material);
    }
}
