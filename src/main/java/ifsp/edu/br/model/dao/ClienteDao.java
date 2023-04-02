package ifsp.edu.br.model.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public @Data class ClienteDao {
    private UUID id;
    private String nome, email, senha, cep, localidade, logradouro, bairro, estado;
}
