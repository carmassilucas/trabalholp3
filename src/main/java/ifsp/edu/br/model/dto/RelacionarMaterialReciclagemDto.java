package ifsp.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public @Data class RelacionarMaterialReciclagemDto {
    private UUID idMaterial, idReciclagem;
    private Float preco;
}
