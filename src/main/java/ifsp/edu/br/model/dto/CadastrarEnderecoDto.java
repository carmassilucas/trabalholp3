package ifsp.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public @Data class CadastrarEnderecoDto {
    private UUID idCliente;
    private String localidade, logradouro, bairro, estado, numero;
    private Object cep;
}
