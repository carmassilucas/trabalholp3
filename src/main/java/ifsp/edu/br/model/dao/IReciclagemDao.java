package ifsp.edu.br.model.dao;

import ifsp.edu.br.model.vo.ReciclagemVo;

public interface IReciclagemDao {
    void cadastrarReciclagem(ReciclagemVo reciclagemVo);
    ReciclagemVo buscarReciclagemByUsuario(String usuario);
}
