package ifsp.edu.br.control;

import ifsp.edu.br.control.exception.CadastrarEnderecoException;
import ifsp.edu.br.model.CadastrarEnderecoModelo;
import ifsp.edu.br.model.dto.CadastrarEnderecoDto;
import ifsp.edu.br.model.util.DtoUtils;

public class CadastrarEnderecoControle {
    private static CadastrarEnderecoControle instancia;
    private final CadastrarEnderecoModelo cadastrarEnderecoModelo;
    private CadastrarEnderecoControle() {
        cadastrarEnderecoModelo = CadastrarEnderecoModelo.getInstancia();
    }

    public static CadastrarEnderecoControle getInstancia() {
        if (instancia == null)
            instancia = new CadastrarEnderecoControle();
        return instancia;
    }

    public void cadastrarEndereco(CadastrarEnderecoDto cadastrarEnderecoDto) throws CadastrarEnderecoException,
            ifsp.edu.br.model.exception.CadastrarEnderecoException {
        if (DtoUtils.verificaSeAtributoNullOrEmpty(cadastrarEnderecoDto))
            throw new CadastrarEnderecoException("Preencha todos os campos para cadastrar o endereço");
        cadastrarEnderecoModelo.cadastrarEndereco(cadastrarEnderecoDto);
    }
}
