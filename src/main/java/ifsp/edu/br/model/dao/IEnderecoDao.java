package ifsp.edu.br.model.dao;

import ifsp.edu.br.model.vo.EnderecoVo;

import java.util.List;
import java.util.UUID;

public interface IEnderecoDao {
    EnderecoVo cadastrarEndereco(EnderecoVo endereco);
    EnderecoVo buscarEnderecoByCepAndNumero(String cep, Integer numero);
    void relacionarClienteEndereco(UUID idCliente, UUID idEndereco);
    List<EnderecoVo> getEnderecosByIdCliente(UUID id);
}
