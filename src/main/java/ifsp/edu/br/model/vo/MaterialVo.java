package ifsp.edu.br.model.vo;

import ifsp.edu.br.model.dto.CadastrarMaterialDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public @Data class MaterialVo {
    private UUID id;
    private String nome, descricao;

    public static MaterialVo toVo(CadastrarMaterialDto cadastrarMaterialDto) {
        return new MaterialVo(
                UUID.randomUUID(),
                cadastrarMaterialDto.getNome(),
                cadastrarMaterialDto.getDescricao()
        );
    }
}
