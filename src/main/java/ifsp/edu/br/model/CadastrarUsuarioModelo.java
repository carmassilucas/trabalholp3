package ifsp.edu.br.model;

import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.dao.IEnderecoDao;
import ifsp.edu.br.model.dao.IUsuarioDao;
import ifsp.edu.br.model.dao.implementation.EnderecoDao;
import ifsp.edu.br.model.dao.implementation.UsuarioDao;
import ifsp.edu.br.model.dto.CadastrarClienteDto;
import ifsp.edu.br.model.exception.EmailDuplicadoException;
import ifsp.edu.br.model.util.MessageDigestUtils;
import ifsp.edu.br.model.vo.ClienteVo;
import ifsp.edu.br.model.vo.EnderecoVo;

import java.security.NoSuchAlgorithmException;

public class CadastrarUsuarioModelo {
    private static CadastrarUsuarioModelo instancia;
    public final IUsuarioDao usuarioDao;
    public final IEnderecoDao enderecoDao;

    private CadastrarUsuarioModelo() {
        usuarioDao = UsuarioDao.getInstancia();
        enderecoDao = EnderecoDao.getInstancia();
    }

    public static CadastrarUsuarioModelo getInstancia() {
        if (instancia == null)
            instancia = new CadastrarUsuarioModelo();
        return instancia;
    }

    public void cadastrarCliente(CadastrarClienteDto dto) throws EmailDuplicadoException,
            CadastrarClienteException {
        if(usuarioDao.buscarUsuarioByEmail(dto.getEmail()) != null)
            throw new EmailDuplicadoException("Endereço de e-mail já cadastrado.");

        try {
            dto.setSenha(MessageDigestUtils.hashSenha(dto.getSenha()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        ClienteVo clienteVo = usuarioDao.cadastrarUsuario(ClienteVo.toVo(dto));
        EnderecoVo enderecoVo = enderecoDao.cadastrarEndereco(EnderecoVo.toVo(dto));
        enderecoDao.relacionarClienteEndereco(clienteVo.getId(), enderecoVo.getId());
    }
}
