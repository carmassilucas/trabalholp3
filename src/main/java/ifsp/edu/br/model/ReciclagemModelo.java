package ifsp.edu.br.model;

import ifsp.edu.br.model.dao.implementation.ClienteDao;
import ifsp.edu.br.model.dao.implementation.EnderecoDao;
import ifsp.edu.br.model.dao.implementation.ReciclagemDao;
import ifsp.edu.br.model.dto.*;
import ifsp.edu.br.model.exception.NegociarMaterialException;
import ifsp.edu.br.model.exception.UsuarioDuplicadoException;
import ifsp.edu.br.model.util.MessageDigestUtils;
import ifsp.edu.br.model.vo.ClienteVo;
import ifsp.edu.br.model.vo.EnderecoVo;
import ifsp.edu.br.model.vo.MaterialReciclagemVo;
import ifsp.edu.br.model.vo.ReciclagemVo;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

public class ReciclagemModelo {
    private static ReciclagemModelo instancia;
    private final ReciclagemDao reciclagemDao;
    private final EnderecoDao enderecoDao;
    private final ClienteDao clienteDao;

    private ReciclagemModelo() {
        reciclagemDao = ReciclagemDao.getInstancia();
        enderecoDao = EnderecoDao.getInstancia();
        clienteDao = ClienteDao.getInstancia();
    }

    public static ReciclagemModelo getInstancia() {
        if (instancia == null)
            instancia = new ReciclagemModelo();
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

    public ReciclagemVo autenticar(AutenticacaoDto dto) {
        return reciclagemDao.buscarReciclagemByUsuarioAndSenha(dto);
    }

    public Integer relacionarMaterialReciclagem(RelacionarMaterialReciclagemDto dto) {
        return reciclagemDao.relacionarMaterialReciclagem(dto);
    }

    public List<MaterialReciclagemVo> buscarMateriais(UUID idReciclagem) {
        return reciclagemDao.buscarMateriais(idReciclagem);
    }

    public void editarPrecoMaterial(RelacionarMaterialReciclagemDto dto) {
        reciclagemDao.editarPrecoMaterial(dto);
    }

    public List<PesquisarMateriaisDto> pesquisarMateriais(PesquisarMateriaisDto dto) {
        return reciclagemDao.pesquisarMateriais(dto);
    }

    public void negociar(NegociarMaterialDto dto) throws NegociarMaterialException {
        ClienteVo cliente = clienteDao.buscarClienteByEmail(dto.getEmailCliente());

        if (cliente == null)
            throw new NegociarMaterialException("Cliente com esse endereço de e-mail não encontrado");

        dto.setIdCliente(cliente.getId());
        reciclagemDao.negociar(dto);
    }
}
