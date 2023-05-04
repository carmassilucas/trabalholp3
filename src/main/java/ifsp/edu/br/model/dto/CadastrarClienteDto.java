package ifsp.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

@AllArgsConstructor()
public @Data class CadastrarClienteDto {

    private String nome, email, senha, localidade, logradouro, bairro, estado;
    private Integer numero;
    private Object cep;

    public Boolean verificaCampos() {
        Field[] campos = getClass().getDeclaredFields();

        for (Field campo : campos) {
            campo.setAccessible(true);
            try {
                if (campo.get(this) == null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                System.err.println(e.getMessage());
            }
        }
        return true;
    }
}
