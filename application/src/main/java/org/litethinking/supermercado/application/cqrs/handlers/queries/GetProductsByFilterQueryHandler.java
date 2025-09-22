package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.ProductoMapper;
import org.litethinking.supermercado.domain.ports.output.RepositorioProductoPort;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetProductsByFilterQuery;
import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handler for GetProductsByFilterQuery.
 * This class is responsible for processing the query to retrieve products by various filters.
 */
@Component
public class GetProductsByFilterQueryHandler {

    private final RepositorioProductoPort repositorioProductoPort;

    public GetProductsByFilterQueryHandler(RepositorioProductoPort repositorioProductoPort) {
        this.repositorioProductoPort = repositorioProductoPort;
    }

    /**
     * Handle the query to retrieve products by various filters.
     *
     * @param query the query with filter parameters
     * @return the list of products matching the filters
     */
    public List<ProductoDto> handle(GetProductsByFilterQuery query) {
        // If codigoBarras is provided, search by that specific field
        if (query.codigoBarras() != null && !query.codigoBarras().isEmpty()) {
            return repositorioProductoPort.findByCodigoBarras(query.codigoBarras())
                    .map(producto -> List.of(ProductoMapper.toDto(producto)))
                    .orElse(List.of());
        }

        // If categoria is provided, search by category
        if (query.categoria() != null && !query.categoria().isEmpty()) {
            return repositorioProductoPort.findByCategoria(query.categoria()).stream()
                    .map(ProductoMapper::toDto)
                    .collect(Collectors.toList());
        }

        // If price range is provided, search by price range
        if (query.precioMinimo() != null && query.precioMaximo() != null) {
            return repositorioProductoPort.findByPrecioBetween(query.precioMinimo(), query.precioMaximo()).stream()
                    .map(ProductoMapper::toDto)
                    .collect(Collectors.toList());
        }

        // If no specific filter is provided, return all products
        return repositorioProductoPort.findAll().stream()
                .map(ProductoMapper::toDto)
                .collect(Collectors.toList());
    }
}