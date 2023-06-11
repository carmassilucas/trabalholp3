package ifsp.edu.br.model.util;

import java.lang.reflect.Field;

public class DtoUtils {
    public static Boolean verificaSeAtributoNullOrEmpty(Object dto) {
        for (Field field : dto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(dto);
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
