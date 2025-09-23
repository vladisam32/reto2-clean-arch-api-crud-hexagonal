package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.ClienteMapper;
import org.litethinking.supermercado.domain.ports.output.RepositorioClientePort;
//import org.litethinking.supermercado.domain.repository.RepositorioCliente;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetClienteByIdQuery;
import org.litethinking.supermercado.shareddto.supermercado.ClienteDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handler for GetClienteByIdQuery.
 * This class is responsible for processing the query to retrieve a customer by its ID.
 */
@Component
public class GetClienteByIdQueryHandler {

    private final RepositorioClientePort repositorioClientePort;

    public GetClienteByIdQueryHandler(RepositorioClientePort repositorioClientePort) {
        this.repositorioClientePort = repositorioClientePort;
    }

    /**
     * Handle the query to retrieve a customer by its ID.
     *
     * @param query the query to get a customer by ID
     * @return the customer if found, empty otherwise
     */
    public Optional<ClienteDto> handle(GetClienteByIdQuery query) {
        return repositorioClientePort.findById(query.id())
                .map(ClienteMapper::toDto);
    }
}