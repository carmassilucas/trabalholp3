package ifsp.edu.br.model;

import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.dao.IEnderecoDao;
import ifsp.edu.br.model.dao.IClienteDao;
import ifsp.edu.br.model.dao.implementation.EnderecoDao;
import ifsp.edu.br.model.dao.implementation.ClienteDao;
import ifsp.edu.br.model.dto.CadastrarClienteDto;
import ifsp.edu.br.model.dto.LoginClienteDto;
import ifsp.edu.br.model.exception.EmailDuplicadoException;
import ifsp.edu.br.model.util.MessageDigestUtils;
import ifsp.edu.br.model.vo.ClienteVo;
import ifsp.edu.br.model.vo.EnderecoVo;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

public class ClienteModelo {
    private static ClienteModelo instancia;
    public final IClienteDao clienteDao;
    public final IEnderecoDao enderecoDao;

    private ClienteModelo() {
        clienteDao = ClienteDao.getInstancia();
        enderecoDao = EnderecoDao.getInstancia();
    }

    public static ClienteModelo getInstancia() {
        if (instancia == null)
            instancia = new ClienteModelo();
        return instancia;
    }

    public void cadastrarCliente(CadastrarClienteDto dto) throws EmailDuplicadoException,
            CadastrarClienteException {
        if(clienteDao.buscarUsuarioByEmail(dto.getEmail()) != null)
            throw new EmailDuplicadoException("Endereço de e-mail já cadastrado.");

        try {
            dto.setSenha(MessageDigestUtils.hashSenha(dto.getSenha()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        ClienteVo clienteVo = clienteDao.cadastrarUsuario(ClienteVo.toVo(dto));
        EnderecoVo enderecoVo = enderecoDao.cadastrarEndereco(EnderecoVo.toVo(dto));
        enderecoDao.relacionarClienteEndereco(clienteVo.getId(), enderecoVo.getId());
    }

    public ClienteVo loginCliente(LoginClienteDto dto) {
        return clienteDao.buscarClienteByEmailAndSenha(dto);
    }

    public List<EnderecoVo> getEnderecosByIdCliente(UUID id) {
        return enderecoDao.getEnderecosByIdCliente(id);
    }
}
