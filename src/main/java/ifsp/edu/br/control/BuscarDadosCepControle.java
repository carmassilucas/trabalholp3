package ifsp.edu.br.control;

import ifsp.edu.br.control.exception.BuscarInformacoesCepException;
import ifsp.edu.br.model.BuscarDadosCepModelo;
import ifsp.edu.br.model.dto.InformacoesCepDto;

public class BuscarDadosCepControle {
    private static BuscarDadosCepControle instancia;
    private BuscarDadosCepModelo modelo;

    private BuscarDadosCepControle() {
        modelo = BuscarDadosCepModelo.getInstancia();
    }

    public static BuscarDadosCepControle getInstancia() {
        if (instancia == null)
            instancia = new BuscarDadosCepControle();
        return instancia;
    }

    public InformacoesCepDto buscarInformacoesCep(Object cep) throws BuscarInformacoesCepException {
        if (cep == null)
            throw new BuscarInformacoesCepException("Por favor, informe o seu cep");
        return modelo.buscarInformacoesCep(cep.toString());
    }
}
