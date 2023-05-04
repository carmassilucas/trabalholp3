package ifsp.edu.br.model.dao;

import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.vo.ClienteVo;
import ifsp.edu.br.model.exception.EmailDuplicadoException;

public interface IUsuarioDao {
    ClienteVo cadastrarUsuario(ClienteVo clienteDao) throws EmailDuplicadoException,
            CadastrarClienteException;
    ClienteVo buscarUsuarioByEmail(String email);
}
