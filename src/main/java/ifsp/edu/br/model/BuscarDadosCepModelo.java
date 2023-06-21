package ifsp.edu.br.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifsp.edu.br.model.dto.InformacoesCepDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BuscarDadosCepModelo {
    private static BuscarDadosCepModelo instancia;
    private BuscarDadosCepModelo() {}

    public static BuscarDadosCepModelo getInstancia() {
        if (instancia == null)
            instancia = new BuscarDadosCepModelo();
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
}
