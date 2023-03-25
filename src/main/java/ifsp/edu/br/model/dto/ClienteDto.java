package ifsp.edu.br.model.dto;

import ifsp.edu.br.model.dao.ClienteDao;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.UUID;

@AllArgsConstructor()
public @Data class ClienteDto {

    private String nome, email, localidade, logradouro, bairro, estado;
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

    public ClienteDao toDao() {
        return new ClienteDao(
                UUID.randomUUID(),
                this.getNome(),
                this.getEmail(),
                this.getCep().toString(),
                this.getLocalidade(),
                this.getLogradouro(),
                this.getBairro(),
                this.getEstado()
        );
    }
}
