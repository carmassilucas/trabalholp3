package ifsp.edu.br.model.dto;

import ifsp.edu.br.model.util.DtoUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class CadastrarMaterialDto {
    private String nome, descricao;
}
