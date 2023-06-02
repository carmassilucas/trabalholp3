package ifsp.edu.br.model;

import ifsp.edu.br.model.dao.IEnderecoDao;
import ifsp.edu.br.model.dao.implementation.EnderecoDao;
import ifsp.edu.br.model.dto.CadastrarEnderecoDto;
import ifsp.edu.br.model.exception.CadastrarEnderecoException;
import ifsp.edu.br.model.vo.EnderecoVo;

public class CadastrarEnderecoModelo {
    private static CadastrarEnderecoModelo instancia;
    private IEnderecoDao enderecoDao;
    private CadastrarEnderecoModelo() {
        enderecoDao = new EnderecoDao();
    }

    public static CadastrarEnderecoModelo getInstancia() {
        if (instancia == null) {
            instancia = new CadastrarEnderecoModelo();
        }
        return instancia;
    }

    public void cadastrarEndereco(CadastrarEnderecoDto cadastrarEnderecoDto) throws CadastrarEnderecoException {
        enderecoDao.cadastrarEndereco(EnderecoVo.toVo(cadastrarEnderecoDto));
    }
}
