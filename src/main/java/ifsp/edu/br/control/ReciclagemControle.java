package ifsp.edu.br.control;

import ifsp.edu.br.control.exception.CadastrarReciclagemException;
import ifsp.edu.br.control.exception.LoginReciclagemException;
import ifsp.edu.br.model.ReciclagemModelo;
import ifsp.edu.br.model.dto.CadastrarReciclagemDto;
import ifsp.edu.br.model.dto.LoginReciclagemDto;
import ifsp.edu.br.model.dto.PesquisarMateriaisDto;
import ifsp.edu.br.model.dto.RelacionarMaterialReciclagemDto;
import ifsp.edu.br.model.exception.UsuarioDuplicadoException;
import ifsp.edu.br.model.util.DtoUtils;
import ifsp.edu.br.model.vo.MaterialReciclagemVo;
import ifsp.edu.br.model.vo.ReciclagemVo;

import java.util.List;
import java.util.UUID;

public class ReciclagemControle {
    private static ReciclagemControle instancia;
    private final ReciclagemModelo reciclagemModelo;

    private ReciclagemControle() {
        reciclagemModelo = ReciclagemModelo.getInstancia();
    }

    public static ReciclagemControle getInstancia() {
        if (instancia == null)
            instancia = new ReciclagemControle();
        return instancia;
    }

    public void cadastrarReciclagem(CadastrarReciclagemDto dto) throws CadastrarReciclagemException, UsuarioDuplicadoException {
        if (DtoUtils.verificaSeAtributoNullOrEmpty(dto))
            throw new CadastrarReciclagemException("Preencha todos os campos para se cadastrar");
        reciclagemModelo.cadastrarReciclagem(dto);
    }

    public ReciclagemVo loginReciclagem(LoginReciclagemDto dto) throws LoginReciclagemException {
        if (DtoUtils.verificaSeAtributoNullOrEmpty(dto))
            throw new LoginReciclagemException("Preencha todos os campos para fazer o login");
        return reciclagemModelo.loginReciclagem(dto);
    }

    public Integer relacionarMaterialReciclagem(RelacionarMaterialReciclagemDto dto) {
        return reciclagemModelo.relacionarMaterialReciclagem(dto);
    }

    public List<MaterialReciclagemVo> buscarMateriais(UUID idReciclagem) {
        return reciclagemModelo.buscarMateriais(idReciclagem);
    }

    public void editarPrecoMaterial(RelacionarMaterialReciclagemDto dto) {
        reciclagemModelo.editarPrecoMaterial(dto);
    }

    public List<PesquisarMateriaisDto> pesquisarMateriais(PesquisarMateriaisDto dto) {
        return reciclagemModelo.pesquisarMateriais(dto);
    }
}
