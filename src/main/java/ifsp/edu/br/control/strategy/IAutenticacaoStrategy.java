package ifsp.edu.br.control.strategy;

import ifsp.edu.br.control.exception.AutenticacaoException;
import ifsp.edu.br.model.dto.AutenticacaoDto;

public interface IAutenticacaoStrategy {
    Object autenticar(AutenticacaoDto dto) throws AutenticacaoException;
}
