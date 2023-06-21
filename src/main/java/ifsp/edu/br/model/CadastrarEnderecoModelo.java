package ifsp.edu.br.model;

import ifsp.edu.br.model.dao.IEnderecoDao;
import ifsp.edu.br.model.dao.implementation.EnderecoDao;
import ifsp.edu.br.model.dto.CadastrarEnderecoDto;
import ifsp.edu.br.model.vo.EnderecoVo;

public class CadastrarEnderecoModelo {
    private static CadastrarEnderecoModelo instancia;
    private final IEnderecoDao enderecoDao;
    private CadastrarEnderecoModelo() {
        enderecoDao = EnderecoDao.getInstancia();
    }

    public static CadastrarEnderecoModelo getInstancia() {
        if (instancia == null)
            instancia = new CadastrarEnderecoModelo();
        return instancia;
    }

    public void cadastrarEndereco(CadastrarEnderecoDto cadastrarEnderecoDto) {
        enderecoDao.cadastrarEndereco(EnderecoVo.toVo(cadastrarEnderecoDto));
    }
}
