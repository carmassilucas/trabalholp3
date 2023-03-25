package ifsp.edu.br.model.dto;

import lombok.Data;

public @Data class InformacoesCepDto {
    private String cep,  logradouro, complemento, bairro, localidade, uf, ibge, gia, ddd, siafi;
}
