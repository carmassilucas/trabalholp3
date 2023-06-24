package ifsp.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public @Data class PesquisarMateriaisDto {
    private UUID idMaterial;
    private String pesquisa, nomeMaterial, nomeReciclagem, logradouro;
    private Integer numero;
    private Float preco;

    public PesquisarMateriaisDto(String pesquisa) {
        this.pesquisa = pesquisa;
    }
}
