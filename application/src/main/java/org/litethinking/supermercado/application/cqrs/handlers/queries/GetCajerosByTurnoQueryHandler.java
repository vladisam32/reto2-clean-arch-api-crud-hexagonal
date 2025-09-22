package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.CajeroMapper;
import org.litethinking.supermercado.domain.ports.output.RepositorioCajeroPort;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetCajerosByTurnoQuery;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handler for GetCajerosByTurnoQuery.
 * This class is responsible for processing the query to retrieve cashiers by their shift.
 */
@Component
public class GetCajerosByTurnoQueryHandler {

    private final RepositorioCajeroPort repositorioCajeroPort;

    public GetCajerosByTurnoQueryHandler(RepositorioCajeroPort repositorioCajeroPort) {
        this.repositorioCajeroPort = repositorioCajeroPort;
    }

    /**
     * Handle the query to retrieve cashiers by their shift.
     *
     * @param query the query to get cashiers by shift
     * @return the list of cashiers in the shift
     */
    public List<CajeroDto> handle(GetCajerosByTurnoQuery query) {
        return repositorioCajeroPort.findByTurno(query.turno()).stream()
                .map(CajeroMapper::toDto)
                .collect(Collectors.toList());
    }
}