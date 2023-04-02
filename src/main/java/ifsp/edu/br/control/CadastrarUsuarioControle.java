package ifsp.edu.br.control;

import ifsp.edu.br.control.exception.BuscarInformacoesCepException;
import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.CadastrarUsuarioModelo;
import ifsp.edu.br.model.dto.ClienteDto;
import ifsp.edu.br.model.dto.InformacoesCepDto;
import ifsp.edu.br.model.exception.EmailDuplicadoException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastrarUsuarioControle {

    private static CadastrarUsuarioControle instancia;

    private final CadastrarUsuarioModelo modelo;

    private CadastrarUsuarioControle() {
        modelo = CadastrarUsuarioModelo.getInstancia();
    }

    public static CadastrarUsuarioControle getInstancia() {
        if (instancia == null) {
            instancia = new CadastrarUsuarioControle();
        }

        return instancia;
    }

    public InformacoesCepDto buscarInformacoesCep(Object cep) throws BuscarInformacoesCepException {
        if (cep == null) {
            throw new BuscarInformacoesCepException("CEP deve ser informado");
        }

        return modelo.buscarInformacoesCep(cep.toString());
    }

    public Integer cadastrarCliente(ClienteDto clienteDto) throws CadastrarClienteException, EmailDuplicadoException {
        if (!clienteDto.verificaCampos()) {
            throw new CadastrarClienteException("Preencha todos os campos para se cadastrar");
        }

        if (!validarEmail(clienteDto.getEmail())) {
            throw new CadastrarClienteException("E-mail inválido");
        }

        return modelo.cadastrarCliente(clienteDto);
    }

    public static boolean validarEmail(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
