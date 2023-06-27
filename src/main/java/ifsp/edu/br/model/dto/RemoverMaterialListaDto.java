package ifsp.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public @Data class RemoverMaterialListaDto {
    private UUID idCliente, idMaterial;
    private String nomeMaterial;
}
