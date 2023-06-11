package ifsp.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

@AllArgsConstructor
public @Data class CadastrarMaterialDto {
    private String nome, descricao;

    public Boolean verificaCampos() {
        for (Field field : getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                if (value == null) {
                    return true;
                }
                if (value instanceof String && ((String) value).isEmpty()) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
