package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.CajeroMapper;
import org.litethinking.supermercado.domain.ports.output.RepositorioCajeroPort;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetCajeroByIdQuery;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handler for GetCajeroByIdQuery.
 * This class is responsible for processing the query to retrieve a cashier by its ID.
 */
@Component
public class GetCajeroByIdQueryHandler {

    private final RepositorioCajeroPort repositorioCajeroPort;

    public GetCajeroByIdQueryHandler(RepositorioCajeroPort repositorioCajeroPort) {
        this.repositorioCajeroPort = repositorioCajeroPort;
    }

    /**
     * Handle the query to retrieve a cashier by its ID.
     *
     * @param query the query to get a cashier by ID
     * @return the cashier if found, empty otherwise
     */
    public Optional<CajeroDto> handle(GetCajeroByIdQuery query) {
        return repositorioCajeroPort.findById(query.id())
                .map(CajeroMapper::toDto);
    }
}
