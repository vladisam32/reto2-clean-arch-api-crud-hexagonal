package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.CajeroMapper;
import org.litethinking.supermercado.domain.repository.RepositorioCajero;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetCajeroByCodigoQuery;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handler for GetCajeroByCodigoQuery.
 * This class is responsible for processing the query to retrieve a cashier by its code.
 */
@Component
public class GetCajeroByCodigoQueryHandler {

    private final RepositorioCajero repositorioCajero;

    public GetCajeroByCodigoQueryHandler(RepositorioCajero repositorioCajero) {
        this.repositorioCajero = repositorioCajero;
    }

    /**
     * Handle the query to retrieve a cashier by its code.
     *
     * @param query the query to get a cashier by code
     * @return the cashier if found, empty otherwise
     */
    public Optional<CajeroDto> handle(GetCajeroByCodigoQuery query) {
        return repositorioCajero.findByCodigo(query.codigo())
                .map(CajeroMapper::toDto);
    }
}