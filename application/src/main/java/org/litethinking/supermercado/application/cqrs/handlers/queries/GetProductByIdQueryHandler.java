package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.ProductoMapper;
import org.litethinking.supermercado.domain.ports.output.RepositorioProductoPort;
import org.litethinking.supermercado.domain.repository.RepositorioProducto;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetProductByIdQuery;
import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handler for GetProductByIdQuery.
 * This class is responsible for processing the query to retrieve a product by its ID.
 */
@Component
public class GetProductByIdQueryHandler {

    private final RepositorioProductoPort repositorioProductoPort;

    public GetProductByIdQueryHandler(RepositorioProductoPort repositorioProductoPort) {
        this.repositorioProductoPort = repositorioProductoPort;
    }

    /**
     * Handle the query to retrieve a product by its ID.
     *
     * @param query the query to get a product by ID
     * @return the product if found, empty otherwise
     */
    public Optional<ProductoDto> handle(GetProductByIdQuery query) {
        return repositorioProductoPort.findById(query.id())
                .map(ProductoMapper::toDto);
    }
}