package ifsp.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor()
public @Data class CadastrarClienteDto {
    private String nome, email, senha, localidade, logradouro, bairro, estado, numero;
    private Object cep;
}
