package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.InventarioMapper;
import org.litethinking.supermercado.domain.model.Producto;
import org.litethinking.supermercado.domain.model.inventario.Inventario;
import org.litethinking.supermercado.domain.ports.output.RepositorioInventarioPort;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetInventarioByProductoIdQuery;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handler for GetInventarioByProductoIdQuery.
 * This class is responsible for processing the query to get an inventory record by its product id.
 */
@Component
public class GetInventarioByProductoIdQueryHandler {

    private final RepositorioInventarioPort repositorioInventarioPort;

    public GetInventarioByProductoIdQueryHandler(RepositorioInventarioPort repositorioInventarioPort) {
        this.repositorioInventarioPort = repositorioInventarioPort;
    }

    /**
     * Handle the query to get an inventory record by its product id.
     *
     * @param query the query to get an inventory record by its product id
     * @return the inventory DTO if found, empty otherwise
     */
    public Optional<InventarioDto> handle(GetInventarioByProductoIdQuery query) {
        Producto producto = Producto.builder()
                .id(query.productoId())
                .build();
        Optional<Inventario> inventarioOptional = repositorioInventarioPort.findByProducto(producto);
        return inventarioOptional.map(InventarioMapper::toDto);
    }
}