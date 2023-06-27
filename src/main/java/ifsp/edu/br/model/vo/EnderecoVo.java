package ifsp.edu.br.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public @Data class EnderecoVo {
    private UUID id;
    private String cep, cidade, logradouro, bairro, uf;
    private Integer numero;

    public EnderecoVo(UUID id) {
        this.id = id;
    }

    public static EnderecoVo toVo(Object dto) {
        try {
            Map<String, Object> fieldValues = new HashMap<>();
            Class<?> dtoClass = dto.getClass();

            for (Field field : dtoClass.getDeclaredFields()) {
                field.setAccessible(true);
                fieldValues.put(field.getName(), field.get(dto));
            }

            return new EnderecoVo(
                    UUID.randomUUID(),
                    fieldValues.get("cep").toString(),
                    fieldValues.get("localidade").toString(),
                    fieldValues.get("logradouro").toString(),
                    fieldValues.get("bairro").toString(),
                    fieldValues.get("estado").toString(),
                    Integer.parseInt(fieldValues.get("numero").toString())
            );
        } catch (IllegalAccessException | NullPointerException | NumberFormatException e) {
            throw new IllegalArgumentException("Erro ao converter DTO em EnderecoVo", e);
        }
    }
}
