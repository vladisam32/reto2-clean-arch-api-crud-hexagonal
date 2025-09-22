package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.ClienteMapper;
import org.litethinking.supermercado.domain.ports.output.RepositorioClientePort;
import org.litethinking.supermercado.domain.repository.RepositorioCliente;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetClienteByEmailQuery;
import org.litethinking.supermercado.shareddto.supermercado.ClienteDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handler for GetClienteByEmailQuery.
 * This class is responsible for processing the query to retrieve a customer by its email.
 */
@Component
public class GetClienteByEmailQueryHandler {

    private final RepositorioClientePort repositorioClientePort;

    public GetClienteByEmailQueryHandler(RepositorioClientePort repositorioClientePort) {
        this.repositorioClientePort = repositorioClientePort;
    }

    /**
     * Handle the query to retrieve a customer by its email.
     *
     * @param query the query to get a customer by email
     * @return the customer if found, empty otherwise
     */
    public Optional<ClienteDto> handle(GetClienteByEmailQuery query) {
        return repositorioClientePort.findByEmail(query.email())
                .map(ClienteMapper::toDto);
    }
}