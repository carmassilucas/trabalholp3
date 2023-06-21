package ifsp.edu.br.control;

import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.CadastrarUsuarioModelo;
import ifsp.edu.br.model.dto.CadastrarClienteDto;
import ifsp.edu.br.model.exception.EmailDuplicadoException;
import ifsp.edu.br.model.util.DtoUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastrarUsuarioControle {
    private static CadastrarUsuarioControle instancia;
    private final CadastrarUsuarioModelo cadastrarClienteModelo;

    private CadastrarUsuarioControle() {
        cadastrarClienteModelo = CadastrarUsuarioModelo.getInstancia();
    }

    public static CadastrarUsuarioControle getInstancia() {
        if (instancia == null)
            instancia = new CadastrarUsuarioControle();
        return instancia;
    }

    public void cadastrarCliente(CadastrarClienteDto cadastrarClienteDto) throws CadastrarClienteException,
            EmailDuplicadoException {
        if (DtoUtils.verificaSeAtributoNullOrEmpty(cadastrarClienteDto))
            throw new CadastrarClienteException("Preencha todos os campos para se cadastrar");
        if (!validarEmail(cadastrarClienteDto.getEmail()))
            throw new CadastrarClienteException("E-mail inválido");
        cadastrarClienteModelo.cadastrarCliente(cadastrarClienteDto);
    }

    public static boolean validarEmail(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
