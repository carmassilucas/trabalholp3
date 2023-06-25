package ifsp.edu.br.control.strategy.implementation;

import ifsp.edu.br.control.strategy.IAutenticacaoStrategy;
import ifsp.edu.br.model.AdministradorModelo;
import ifsp.edu.br.model.dto.AutenticacaoDto;

public class AutenticacaoAdministradorStrategy implements IAutenticacaoStrategy {
    private static AutenticacaoAdministradorStrategy instancia;
    private final AdministradorModelo administradorModelo;

    private AutenticacaoAdministradorStrategy() {
        administradorModelo = AdministradorModelo.getInstancia();
    }

    public static AutenticacaoAdministradorStrategy getInstancia() {
        if (instancia == null)
            instancia = new AutenticacaoAdministradorStrategy();
        return instancia;
    }

    @Override
    public Object autenticar(AutenticacaoDto dto) {
        return administradorModelo.autenticar(dto);
    }
}
