package ifsp.edu.br.model.vo;

import ifsp.edu.br.model.dto.CadastrarClienteDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public @Data class ClienteVo {
    private UUID id;
    private String nome, email, senha;
    private List<EnderecoVo> enderecos;

    public static ClienteVo toVo(CadastrarClienteDto cadastrarClienteDto) {
        return new ClienteVo(
                UUID.randomUUID(),
                cadastrarClienteDto.getNome(),
                cadastrarClienteDto.getEmail(),
                cadastrarClienteDto.getSenha(),
                null
        );
    }
}
