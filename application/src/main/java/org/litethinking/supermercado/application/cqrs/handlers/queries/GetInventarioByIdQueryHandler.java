package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.InventarioMapper;
import org.litethinking.supermercado.domain.model.inventario.Inventario;
import org.litethinking.supermercado.domain.ports.output.RepositorioInventarioPort;
import org.litethinking.supermercado.domain.repository.inventario.RepositorioInventario;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetInventarioByIdQuery;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handler for GetInventarioByIdQuery.
 * This class is responsible for processing the query to get an inventory record by its id.
 */
@Component
public class GetInventarioByIdQueryHandler {

    private final RepositorioInventarioPort repositorioInventarioPort;

    public GetInventarioByIdQueryHandler(RepositorioInventarioPort repositorioInventarioPort) {
        this.repositorioInventarioPort = repositorioInventarioPort;
    }

    /**
     * Handle the query to get an inventory record by its id.
     *
     * @param query the query to get an inventory record by its id
     * @return the inventory DTO if found, empty otherwise
     */
    public Optional<InventarioDto> handle(GetInventarioByIdQuery query) {
        Optional<Inventario> inventarioOptional = repositorioInventarioPort.findById(query.id());
        return inventarioOptional.map(InventarioMapper::toDto);
    }
}