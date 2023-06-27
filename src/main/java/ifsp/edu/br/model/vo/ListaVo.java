package ifsp.edu.br.model.vo;

import ifsp.edu.br.model.dto.CadastrarMaterialListaDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public @Data class ListaVo {
    private UUID idCliente, idMaterial, idReciclagem;

    public static ListaVo toVo(CadastrarMaterialListaDto dto) {
        return new ListaVo(
                dto.getIdCliente(),
                dto.getIdMaterial(),
                dto.getIdReciclagem()
        );
    }
}
