package ifsp.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public @Data class NegociarMaterialDto {
    private UUID idReciclagem, idMaterial, idCliente;
    private String emailCliente;
    private Float peso, preco;
}
