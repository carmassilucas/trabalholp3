package ifsp.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class CadastrarMaterialDto {
    private String nome, descricao;
}
