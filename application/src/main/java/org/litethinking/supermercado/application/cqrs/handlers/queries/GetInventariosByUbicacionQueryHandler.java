package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.InventarioMapper;
import org.litethinking.supermercado.domain.model.inventario.Inventario;
import org.litethinking.supermercado.domain.ports.output.RepositorioInventarioPort;
import org.litethinking.supermercado.domain.repository.inventario.RepositorioInventario;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetInventariosByUbicacionQuery;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handler for GetInventariosByUbicacionQuery.
 * This class is responsible for processing the query to get inventory records by their location.
 */
@Component
public class GetInventariosByUbicacionQueryHandler {

    private final RepositorioInventarioPort repositorioInventarioPort;

    public GetInventariosByUbicacionQueryHandler(RepositorioInventarioPort repositorioInventarioPort) {
        this.repositorioInventarioPort = repositorioInventarioPort;
    }

    /**
     * Handle the query to get inventory records by their location.
     *
     * @param query the query to get inventory records by their location
     * @return the list of inventory DTOs in the location
     */
    public List<InventarioDto> handle(GetInventariosByUbicacionQuery query) {
        List<Inventario> inventarios = repositorioInventarioPort.findByUbicacion(query.ubicacion());
        return inventarios.stream()
                .map(InventarioMapper::toDto)
                .collect(Collectors.toList());
    }
}