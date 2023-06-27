package ifsp.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public @Data class CadastrarMaterialListaDto {
    private UUID idCliente, idMaterial, idReciclagem;
    private String nomeMaterial, nomeReciclagem;
}
