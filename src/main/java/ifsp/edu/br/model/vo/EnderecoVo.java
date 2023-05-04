package ifsp.edu.br.model.vo;

import ifsp.edu.br.model.dto.CadastrarClienteDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public @Data class EnderecoVo {
    private UUID id;
    private String cep, cidade, logradouro, bairro, uf;
    private Integer numero;

    public static EnderecoVo toVo(CadastrarClienteDto cadastrarClienteDto) {
        return new EnderecoVo(
                UUID.randomUUID(),
                cadastrarClienteDto.getCep().toString(),
                cadastrarClienteDto.getLocalidade(),
                cadastrarClienteDto.getLogradouro(),
                cadastrarClienteDto.getBairro(),
                cadastrarClienteDto.getEstado(),
                cadastrarClienteDto.getNumero()
        );
    }
}
