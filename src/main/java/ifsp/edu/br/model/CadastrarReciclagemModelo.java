package ifsp.edu.br.model;

import ifsp.edu.br.model.dao.implementation.EnderecoDao;
import ifsp.edu.br.model.dao.implementation.ReciclagemDao;
import ifsp.edu.br.model.dto.CadastrarReciclagemDto;
import ifsp.edu.br.model.exception.UsuarioDuplicadoException;
import ifsp.edu.br.model.util.MessageDigestUtils;
import ifsp.edu.br.model.vo.EnderecoVo;
import ifsp.edu.br.model.vo.ReciclagemVo;

import java.security.NoSuchAlgorithmException;

public class CadastrarReciclagemModelo {
    private static CadastrarReciclagemModelo instancia;
    private final ReciclagemDao reciclagemDao;
    private final EnderecoDao enderecoDao;

    private CadastrarReciclagemModelo() {
        reciclagemDao = ReciclagemDao.getInstancia();
        enderecoDao = EnderecoDao.getInstancia();
    }

    public static CadastrarReciclagemModelo getInstancia() {
        if (instancia == null)
            instancia = new CadastrarReciclagemModelo();
        return instancia;
    }

    public void cadastrarReciclagem(CadastrarReciclagemDto dto) throws UsuarioDuplicadoException {
        if (reciclagemDao.buscarReciclagemByUsuario(dto.getUsuario()) != null)
            throw new UsuarioDuplicadoException("Nome de usuário já cadastrado");

        EnderecoVo enderecoVo = enderecoDao.cadastrarEndereco(EnderecoVo.toVo(dto));

        try {
            dto.setSenha(MessageDigestUtils.hashSenha(dto.getSenha()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        reciclagemDao.cadastrarReciclagem(ReciclagemVo.toVo(dto, enderecoVo.getId()));
    }
}
