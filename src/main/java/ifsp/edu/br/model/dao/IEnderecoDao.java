package ifsp.edu.br.model.dao;

import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.exception.CadastrarEnderecoException;
import ifsp.edu.br.model.vo.EnderecoVo;

import java.util.UUID;

public interface IEnderecoDao {
    void cadastrarEndereco(EnderecoVo endereco, UUID idCliente) throws CadastrarClienteException, CadastrarEnderecoException;
    EnderecoVo buscarEnderecoByCepAndNumero(String cep, Integer numero);
    void relacionarClienteEndereco(UUID idCliente, UUID idEndereco) throws CadastrarEnderecoException;
}
