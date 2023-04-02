package ifsp.edu.br.model.repostitory;

import ifsp.edu.br.model.dao.ClienteDao;
import ifsp.edu.br.model.exception.EmailDuplicadoException;

public interface ICadastrarUsuarioRepository {
    Integer cadastrarUsuario(ClienteDao clienteDao) throws EmailDuplicadoException;
    ClienteDao buscarUsuarioByEmail(String email);
}
