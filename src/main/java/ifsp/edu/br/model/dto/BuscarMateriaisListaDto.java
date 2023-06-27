package ifsp.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class BuscarMateriaisListaDto {
    private String nomeMaterial, nomeReciclagem, enderecoReciclagem;
    private Integer numero;
    private Float preco;
}
