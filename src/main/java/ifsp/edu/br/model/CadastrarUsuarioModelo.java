package ifsp.edu.br.model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.dao.IEnderecoDao;
import ifsp.edu.br.model.dao.implementation.EnderecoDao;
import ifsp.edu.br.model.dto.CadastrarClienteDto;
import ifsp.edu.br.model.dto.InformacoesCepDto;
import ifsp.edu.br.model.exception.CadastrarEnderecoException;
import ifsp.edu.br.model.exception.EmailDuplicadoException;
import ifsp.edu.br.model.dao.IUsuarioDao;
import ifsp.edu.br.model.dao.implementation.UsuarioDao;
import ifsp.edu.br.model.vo.ClienteVo;
import ifsp.edu.br.model.vo.EnderecoVo;

public class CadastrarUsuarioModelo {

    private static CadastrarUsuarioModelo instancia;

    public IUsuarioDao usuarioDao;
    public IEnderecoDao enderecoDao;

    private CadastrarUsuarioModelo() {
        usuarioDao = new UsuarioDao();
        enderecoDao = new EnderecoDao();
    }

    public static CadastrarUsuarioModelo getInstancia() {
        if (instancia == null) {
            instancia = new CadastrarUsuarioModelo();
        }

        return instancia;
    }

    public InformacoesCepDto buscarInformacoesCep(String cep) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://viacep.com.br/ws/" + cep + "/json/"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return new ObjectMapper().readValue(response.body(), InformacoesCepDto.class);
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao tentar buscar cep: " + e.getMessage());
        }
        return null;
    }

    public void cadastrarCliente(CadastrarClienteDto cadastrarClienteDto) throws EmailDuplicadoException,
            CadastrarClienteException, CadastrarEnderecoException {
        if(usuarioDao.buscarUsuarioByEmail(cadastrarClienteDto.getEmail()) != null) {
            throw new EmailDuplicadoException("Endereço de e-mail já cadastrado.");
        }
        ClienteVo clienteVo = usuarioDao.cadastrarUsuario(ClienteVo.toVo(cadastrarClienteDto));
        enderecoDao.cadastrarEndereco(EnderecoVo.toVo(cadastrarClienteDto), clienteVo.getId());
    }
}
