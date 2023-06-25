package ifsp.edu.br.control;

import ifsp.edu.br.control.exception.AutenticacaoException;
import ifsp.edu.br.control.strategy.IAutenticacaoStrategy;
import ifsp.edu.br.control.strategy.implementation.AutenticacaoAdministradorStrategy;
import ifsp.edu.br.control.strategy.implementation.AutenticacaoClienteStrategy;
import ifsp.edu.br.control.strategy.implementation.AutenticacaoReciclagemStrategy;
import ifsp.edu.br.model.dto.AutenticacaoDto;
import ifsp.edu.br.model.util.DtoUtils;

public class AutenticacaoControle {
    private static AutenticacaoControle instancia;

    private AutenticacaoControle() {
    }

    public static AutenticacaoControle getInstancia() {
        if (instancia == null)
            instancia = new AutenticacaoControle();
        return instancia;
    }

    public Object autenticar(AutenticacaoDto dto) throws AutenticacaoException {
        if (DtoUtils.verificaSeAtributoNullOrEmpty(dto))
            throw new AutenticacaoException("Preencha ambos os campos para se autenticar");

        String perfil = dto.getPerfil();

        IAutenticacaoStrategy autenticacaoStrategy = switch (perfil) {
            case "Cliente" -> AutenticacaoClienteStrategy.getInstancia();
            case "Reciclagem" -> AutenticacaoReciclagemStrategy.getInstancia();
            case "Administrador" -> AutenticacaoAdministradorStrategy.getInstancia();
            default -> throw new AutenticacaoException("Perfil selecionado não reconhecido pelo sistema");
        };

        return autenticacaoStrategy.autenticar(dto);
    }
}
