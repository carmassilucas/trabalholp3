package ifsp.edu.br.model.vo;

import ifsp.edu.br.model.dto.CadastrarClienteDto;
import ifsp.edu.br.model.dto.CadastrarEnderecoDto;
import ifsp.edu.br.model.dto.CadastrarReciclagemDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public @Data class EnderecoVo {
    private UUID id;
    private String cep, cidade, logradouro, bairro, uf;
    private Integer numero;

    public EnderecoVo(UUID id) {
        this.id = id;
    }

    // TODO: refatorar métodos toVo
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

    public static EnderecoVo toVo(CadastrarReciclagemDto cadastrarReciclagemDto) {
        return new EnderecoVo(
                UUID.randomUUID(),
                cadastrarReciclagemDto.getCep().toString(),
                cadastrarReciclagemDto.getLocalidade(),
                cadastrarReciclagemDto.getLogradouro(),
                cadastrarReciclagemDto.getBairro(),
                cadastrarReciclagemDto.getEstado(),
                Integer.parseInt(cadastrarReciclagemDto.getNumero())
        );
    }
}
