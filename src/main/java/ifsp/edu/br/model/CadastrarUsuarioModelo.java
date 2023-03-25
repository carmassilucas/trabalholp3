package ifsp.edu.br.model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifsp.edu.br.model.dao.ClienteDao;
import ifsp.edu.br.model.dto.ClienteDto;
import ifsp.edu.br.model.dto.InformacoesCepDto;
import ifsp.edu.br.model.repostitory.ICadastrarUsuarioRepository;
import ifsp.edu.br.model.repostitory.implementation.CadastrarUsuarioRepository;

public class CadastrarUsuarioModelo {

    public ICadastrarUsuarioRepository cadastrarUsuarioRepository;

    public CadastrarUsuarioModelo() {
        cadastrarUsuarioRepository = new CadastrarUsuarioRepository();
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

    public Integer cadastrarCliente(ClienteDto clienteDto) {
        ClienteDao clienteDao = clienteDto.toDao();

        return cadastrarUsuarioRepository.cadastrarUsuario(clienteDao);
    }
}
