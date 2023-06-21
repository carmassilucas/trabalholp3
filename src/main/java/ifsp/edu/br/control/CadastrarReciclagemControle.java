package ifsp.edu.br.control;

import ifsp.edu.br.control.exception.CadastrarReciclagemException;
import ifsp.edu.br.model.CadastrarReciclagemModelo;
import ifsp.edu.br.model.dto.CadastrarReciclagemDto;
import ifsp.edu.br.model.exception.UsuarioDuplicadoException;
import ifsp.edu.br.model.util.DtoUtils;

public class CadastrarReciclagemControle {
    private static CadastrarReciclagemControle instancia;
    private final CadastrarReciclagemModelo cadastrarReciclagemModelo;

    private CadastrarReciclagemControle() {
        cadastrarReciclagemModelo = CadastrarReciclagemModelo.getInstancia();
    }

    public static CadastrarReciclagemControle getInstancia() {
        if (instancia == null)
            instancia = new CadastrarReciclagemControle();
        return instancia;
    }

    public void cadastrarReciclagem(CadastrarReciclagemDto dto) throws CadastrarReciclagemException, UsuarioDuplicadoException {
        if (DtoUtils.verificaSeAtributoNullOrEmpty(dto))
            throw new CadastrarReciclagemException("Preencha todos os campos para se cadastrar");
        cadastrarReciclagemModelo.cadastrarReciclagem(dto);
    }
}
