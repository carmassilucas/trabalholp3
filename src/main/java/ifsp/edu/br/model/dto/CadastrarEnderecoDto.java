package ifsp.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

@AllArgsConstructor
public @Data class CadastrarEnderecoDto {
    private String localidade, logradouro, bairro, estado, numero;
    private Object cep;
}
