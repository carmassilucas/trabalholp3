package ifsp.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class CadastrarReciclagemDto {
    private String nome, usuario, senha, localidade, logradouro, bairro, estado, numero;
    private Object cep;
}
