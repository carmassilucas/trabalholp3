package ifsp.edu.br.control.strategy.implementation;

import ifsp.edu.br.control.strategy.IAutenticacaoStrategy;
import ifsp.edu.br.model.ClienteModelo;
import ifsp.edu.br.model.dto.AutenticacaoDto;

public class AutenticacaoClienteStrategy implements IAutenticacaoStrategy{
    private static AutenticacaoClienteStrategy instancia;
    private final ClienteModelo clienteModelo;

    private AutenticacaoClienteStrategy() {
        clienteModelo = ClienteModelo.getInstancia();
    }

    public static AutenticacaoClienteStrategy getInstancia() {
        if (instancia == null)
            instancia = new AutenticacaoClienteStrategy();
        return instancia;
    }

    @Override
    public Object autenticar(AutenticacaoDto dto) {
        return clienteModelo.autenticar(dto);
    }
}
