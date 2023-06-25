package ifsp.edu.br.model.dao;

import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.dto.AutenticacaoDto;
import ifsp.edu.br.model.exception.EmailDuplicadoException;
import ifsp.edu.br.model.vo.ClienteVo;

public interface IClienteDao {
    ClienteVo cadastrarUsuario(ClienteVo clienteDao) throws EmailDuplicadoException,
            CadastrarClienteException;
    ClienteVo buscarUsuarioByEmail(String email);

    ClienteVo buscarClienteByEmailAndSenha(AutenticacaoDto dto);
}
