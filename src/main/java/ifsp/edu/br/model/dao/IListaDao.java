package ifsp.edu.br.model.dao;

import ifsp.edu.br.model.dto.BuscarMateriaisListaDto;
import ifsp.edu.br.model.dto.RemoverMaterialListaDto;
import ifsp.edu.br.model.vo.ListaVo;

import java.util.List;
import java.util.UUID;

public interface IListaDao {
    void cadastrarMaterialLista(ListaVo lista);
    Boolean verificaIfCLienteMaterialExists(ListaVo lista);
    List<BuscarMateriaisListaDto> getListaMateriais(UUID idCliente);
    void removerMaterialLista(RemoverMaterialListaDto dto);
}
