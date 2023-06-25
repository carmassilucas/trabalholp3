package ifsp.edu.br.control.strategy.implementation;

import ifsp.edu.br.control.strategy.IAutenticacaoStrategy;
import ifsp.edu.br.model.ReciclagemModelo;
import ifsp.edu.br.model.dto.AutenticacaoDto;

public class AutenticacaoReciclagemStrategy implements IAutenticacaoStrategy {
    private static AutenticacaoReciclagemStrategy instancia;
    private static ReciclagemModelo reciclagemModelo;

    private AutenticacaoReciclagemStrategy() {
        reciclagemModelo = ReciclagemModelo.getInstancia();
    }

    public static AutenticacaoReciclagemStrategy getInstancia() {
        if (instancia == null)
            instancia = new AutenticacaoReciclagemStrategy();
        return instancia;
    }

    @Override
    public Object autenticar(AutenticacaoDto dto) {
        return reciclagemModelo.autenticar(dto);
    }
}
