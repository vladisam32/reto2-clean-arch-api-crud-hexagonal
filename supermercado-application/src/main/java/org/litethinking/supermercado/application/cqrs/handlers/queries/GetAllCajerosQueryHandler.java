package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.CajeroMapper;
import org.litethinking.supermercado.domain.ports.output.RepositorioCajeroPort;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetAllCajerosQuery;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handler for GetAllCajerosQuery.
 * This class is responsible for processing the query to retrieve all cashiers.
 */
@Component
public class GetAllCajerosQueryHandler {

    private final RepositorioCajeroPort repositorioCajeroPort;

    public GetAllCajerosQueryHandler(RepositorioCajeroPort repositorioCajeroPort) {
        this.repositorioCajeroPort = repositorioCajeroPort;
    }

    /**
     * Handle the query to retrieve all cashiers.
     *
     * @param query the query to get all cashiers
     * @return the list of all cashiers
     */
    public List<CajeroDto> handle(GetAllCajerosQuery query) {
        return repositorioCajeroPort.findAll().stream()
                .map(CajeroMapper::toDto)
                .collect(Collectors.toList());
    }
}
