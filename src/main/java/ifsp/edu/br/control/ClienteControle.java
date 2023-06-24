package ifsp.edu.br.control;

import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.control.exception.LoginClienteException;
import ifsp.edu.br.model.ClienteModelo;
import ifsp.edu.br.model.dto.CadastrarClienteDto;
import ifsp.edu.br.model.dto.LoginClienteDto;
import ifsp.edu.br.model.exception.EmailDuplicadoException;
import ifsp.edu.br.model.util.DtoUtils;
import ifsp.edu.br.model.vo.ClienteVo;
import ifsp.edu.br.model.vo.EnderecoVo;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClienteControle {
    private static ClienteControle instancia;
    private final ClienteModelo clienteModelo;

    private ClienteControle() {
        clienteModelo = ClienteModelo.getInstancia();
    }

    public static ClienteControle getInstancia() {
        if (instancia == null)
            instancia = new ClienteControle();
        return instancia;
    }

    public void cadastrarCliente(CadastrarClienteDto dto) throws CadastrarClienteException,
            EmailDuplicadoException {
        if (DtoUtils.verificaSeAtributoNullOrEmpty(dto))
            throw new CadastrarClienteException("Preencha todos os campos para se cadastrar");
        if (!validarEmail(dto.getEmail()))
            throw new CadastrarClienteException("E-mail inválido");
        clienteModelo.cadastrarCliente(dto);
    }

    public ClienteVo loginCliente(LoginClienteDto dto) throws LoginClienteException {
        if (DtoUtils.verificaSeAtributoNullOrEmpty(dto))
            throw new LoginClienteException("Preencha todos os campos para fazer o login");
        return clienteModelo.loginCliente(dto);
    }

    public List<EnderecoVo> getEnderecosByIdCliente(UUID id) {
        return clienteModelo.getEnderecosByIdCliente(id);
    }

    private static boolean validarEmail(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
