package ifsp.edu.br.model;

import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.dao.IEnderecoDao;
import ifsp.edu.br.model.dao.IUsuarioDao;
import ifsp.edu.br.model.dao.implementation.EnderecoDao;
import ifsp.edu.br.model.dao.implementation.UsuarioDao;
import ifsp.edu.br.model.dto.CadastrarClienteDto;
import ifsp.edu.br.model.exception.CadastrarEnderecoException;
import ifsp.edu.br.model.exception.EmailDuplicadoException;
import ifsp.edu.br.model.util.MessageDigestUtils;
import ifsp.edu.br.model.vo.ClienteVo;
import ifsp.edu.br.model.vo.EnderecoVo;

import java.security.NoSuchAlgorithmException;

public class CadastrarUsuarioModelo {
    private static CadastrarUsuarioModelo instancia;
    public IUsuarioDao usuarioDao;
    public IEnderecoDao enderecoDao;

    private CadastrarUsuarioModelo() {
        usuarioDao = new UsuarioDao();
        enderecoDao = new EnderecoDao();
    }

    public static CadastrarUsuarioModelo getInstancia() {
        if (instancia == null) {
            instancia = new CadastrarUsuarioModelo();
        }
        return instancia;
    }

    public void cadastrarCliente(CadastrarClienteDto cadastrarClienteDto) throws EmailDuplicadoException,
            CadastrarClienteException, CadastrarEnderecoException, NoSuchAlgorithmException {
        if(usuarioDao.buscarUsuarioByEmail(cadastrarClienteDto.getEmail()) != null) {
            throw new EmailDuplicadoException("Endereço de e-mail já cadastrado.");
        }

        cadastrarClienteDto.setSenha(MessageDigestUtils.hashSenha(cadastrarClienteDto.getSenha()));
        ClienteVo clienteVo = usuarioDao.cadastrarUsuario(ClienteVo.toVo(cadastrarClienteDto));

        EnderecoVo enderecoVo = enderecoDao.cadastrarEndereco(EnderecoVo.toVo(cadastrarClienteDto));

        enderecoDao.relacionarClienteEndereco(clienteVo.getId(), enderecoVo.getId());
    }
}
