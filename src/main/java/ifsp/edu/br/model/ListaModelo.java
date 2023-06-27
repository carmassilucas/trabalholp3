package ifsp.edu.br.model;

import ifsp.edu.br.model.dao.implementation.ListaDao;
import ifsp.edu.br.model.dao.implementation.MaterialDao;
import ifsp.edu.br.model.dao.implementation.ReciclagemDao;
import ifsp.edu.br.model.dto.BuscarMateriaisListaDto;
import ifsp.edu.br.model.dto.CadastrarMaterialListaDto;
import ifsp.edu.br.model.dto.RemoverMaterialListaDto;
import ifsp.edu.br.model.vo.ListaVo;

import java.util.List;
import java.util.UUID;

public class ListaModelo {
    private static ListaModelo instancia;
    private final MaterialDao materialDao;
    private final ReciclagemDao reciclagemDao;
    private final ListaDao listaDao;

    private ListaModelo() {
        materialDao = MaterialDao.getInstancia();
        reciclagemDao = ReciclagemDao.getInstancia();
        listaDao = ListaDao.getInstancia();
    }

    public static ListaModelo getInstancia() {
        if (instancia == null)
            instancia = new ListaModelo();
        return instancia;
    }

    public void cadastrarMaterialLista(CadastrarMaterialListaDto dto) {
        dto.setIdMaterial(materialDao.buscarMaterialByNome(dto.getNomeMaterial()).getId());
        dto.setIdReciclagem(reciclagemDao.buscarReciclagemByNome(dto.getNomeReciclagem()).getId());

        listaDao.cadastrarMaterialLista(ListaVo.toVo(dto));
    }

    public List<BuscarMateriaisListaDto> getListaMateriais(UUID idCLiente) {
        return listaDao.getListaMateriais(idCLiente);
    }

    public void removerMaterialLista(RemoverMaterialListaDto dto) {
        dto.setIdMaterial(materialDao.buscarMaterialByNome(dto.getNomeMaterial()).getId());
        listaDao.removerMaterialLista(dto);
    }
}
