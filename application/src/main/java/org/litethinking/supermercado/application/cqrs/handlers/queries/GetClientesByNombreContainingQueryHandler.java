package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.ClienteMapper;
import org.litethinking.supermercado.domain.ports.output.RepositorioClientePort;
import org.litethinking.supermercado.domain.repository.RepositorioCliente;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetClientesByNombreContainingQuery;
import org.litethinking.supermercado.shareddto.supermercado.ClienteDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handler for GetClientesByNombreContainingQuery.
 * This class is responsible for processing the query to retrieve customers by name containing a substring.
 */
@Component
public class GetClientesByNombreContainingQueryHandler {

    private final RepositorioClientePort repositorioClientePort;

    public GetClientesByNombreContainingQueryHandler(RepositorioClientePort repositorioClientePort) {
        this.repositorioClientePort = repositorioClientePort;
    }

    /**
     * Handle the query to retrieve customers by name containing a substring.
     *
     * @param query the query to get customers by name
     * @return the list of customers with matching names
     */
    public List<ClienteDto> handle(GetClientesByNombreContainingQuery query) {
        return repositorioClientePort.findByNombreContaining(query.nombre()).stream()
                .map(ClienteMapper::toDto)
                .collect(Collectors.toList());
    }
}