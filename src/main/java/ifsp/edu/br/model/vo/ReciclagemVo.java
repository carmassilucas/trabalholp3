package ifsp.edu.br.model.vo;

import ifsp.edu.br.model.dto.CadastrarReciclagemDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public @Data class ReciclagemVo {
    private UUID id;
    private EnderecoVo endereco;
    private String nome, usuario, senha;

    public static ReciclagemVo toVo(CadastrarReciclagemDto dto, UUID idEndereco) {
        return new ReciclagemVo(
                UUID.randomUUID(),
                new EnderecoVo(
                        idEndereco,
                        dto.getCep().toString(),
                        dto.getLocalidade(),
                        dto.getLogradouro(),
                        dto.getBairro(),
                        dto.getEstado(),
                        Integer.parseInt(dto.getNumero())
                ),
                dto.getNome(),
                dto.getUsuario(),
                dto.getSenha()
        );
    }
}
