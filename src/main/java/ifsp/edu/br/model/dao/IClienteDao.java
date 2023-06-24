package ifsp.edu.br.model.dao;

import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.dto.LoginClienteDto;
import ifsp.edu.br.model.vo.ClienteVo;
import ifsp.edu.br.model.exception.EmailDuplicadoException;
import ifsp.edu.br.model.vo.EnderecoVo;

import java.util.List;
import java.util.UUID;

public interface IClienteDao {
    ClienteVo cadastrarUsuario(ClienteVo clienteDao) throws EmailDuplicadoException,
            CadastrarClienteException;
    ClienteVo buscarUsuarioByEmail(String email);

    ClienteVo buscarClienteByEmailAndSenha(LoginClienteDto dto);
}
