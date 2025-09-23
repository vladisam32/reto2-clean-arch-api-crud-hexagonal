package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.ProductoMapper;
import org.litethinking.supermercado.domain.ports.output.RepositorioProductoPort;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetAllProductsQuery;
import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handler for GetAllProductsQuery.
 * This class is responsible for processing the query to retrieve all products.
 */
@Component
public class GetAllProductsQueryHandler {

    private final RepositorioProductoPort repositorioProductoPort;

    public GetAllProductsQueryHandler(RepositorioProductoPort repositorioProductoPort) {
        this.repositorioProductoPort = repositorioProductoPort;
    }

    /**
     * Handle the query to retrieve all products.
     *
     * @param query the query to get all products
     * @return the list of all products
     */
    public List<ProductoDto> handle(GetAllProductsQuery query) {
        return repositorioProductoPort.findAll().stream()
                .map(ProductoMapper::toDto)
                .collect(Collectors.toList());
    }
}