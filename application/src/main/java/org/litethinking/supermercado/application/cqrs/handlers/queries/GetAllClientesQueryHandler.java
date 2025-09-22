package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.ClienteMapper;
import org.litethinking.supermercado.domain.repository.RepositorioCliente;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetAllClientesQuery;
import org.litethinking.supermercado.shareddto.supermercado.ClienteDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handler for GetAllClientesQuery.
 * This class is responsible for processing the query to retrieve all customers.
 */
@Component
public class GetAllClientesQueryHandler {

    private final RepositorioCliente repositorioCliente;

    public GetAllClientesQueryHandler(RepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    /**
     * Handle the query to retrieve all customers.
     *
     * @param query the query to get all customers
     * @return the list of all customers
     */
    public List<ClienteDto> handle(GetAllClientesQuery query) {
        return repositorioCliente.findAll().stream()
                .map(ClienteMapper::toDto)
                .collect(Collectors.toList());
    }
}