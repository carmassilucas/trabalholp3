package ifsp.edu.br.model.vo;

import ifsp.edu.br.model.dto.CadastrarClienteDto;
import ifsp.edu.br.model.dto.CadastrarEnderecoDto;
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
            Integer.parseInt(cadastrarClienteDto.getNumero())
        );
    }

    public static EnderecoVo toVo(CadastrarEnderecoDto cadastrarEnderecoDto) {
        return new EnderecoVo(
            UUID.randomUUID(),
            cadastrarEnderecoDto.getCep().toString(),
            cadastrarEnderecoDto.getLocalidade(),
            cadastrarEnderecoDto.getLogradouro(),
            cadastrarEnderecoDto.getBairro(),
            cadastrarEnderecoDto.getEstado(),
            Integer.parseInt(cadastrarEnderecoDto.getNumero())
        );
    }
}
