package ifsp.edu.br.control;

import ifsp.edu.br.control.exception.AdicionarMaterialListaException;
import ifsp.edu.br.model.ListaModelo;
import ifsp.edu.br.model.dto.BuscarMateriaisListaDto;
import ifsp.edu.br.model.dto.CadastrarMaterialListaDto;
import ifsp.edu.br.model.dto.RemoverMaterialListaDto;

import java.util.List;
import java.util.UUID;

public class ListaControle {
    private static ListaControle instancia;
    private final ListaModelo listaModelo;

    private ListaControle() {
        listaModelo = ListaModelo.getInstancia();
    }

    public static ListaControle getInstancia() {
        if (instancia == null)
            instancia = new ListaControle();
        return instancia;
    }

    public void cadastrarMaterialLista(CadastrarMaterialListaDto dto) throws AdicionarMaterialListaException {
        listaModelo.cadastrarMaterialLista(dto);
    }

    public List<BuscarMateriaisListaDto> getListaMateriais(UUID idCliente) {
        return listaModelo.getListaMateriais(idCliente);
    }

    public void removerMaterialLista(RemoverMaterialListaDto dto) {
        listaModelo.removerMaterialLista(dto);
    }
}
