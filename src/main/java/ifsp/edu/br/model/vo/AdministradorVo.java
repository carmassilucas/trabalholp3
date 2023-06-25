package ifsp.edu.br.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public @Data class AdministradorVo {
    private UUID id;
    private String usuario, senha;
}
