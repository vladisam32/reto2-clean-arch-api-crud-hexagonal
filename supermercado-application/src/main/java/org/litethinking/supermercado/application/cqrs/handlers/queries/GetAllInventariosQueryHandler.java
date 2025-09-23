package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.InventarioMapper;
import org.litethinking.supermercado.domain.model.inventario.Inventario;
import org.litethinking.supermercado.domain.ports.output.RepositorioInventarioPort;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetAllInventariosQuery;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handler for GetAllInventariosQuery.
 * This class is responsible for processing the query to get all inventory records.
 */
@Component
public class GetAllInventariosQueryHandler {

    private final RepositorioInventarioPort repositorioInventarioPort;

    public GetAllInventariosQueryHandler(RepositorioInventarioPort repositorioInventarioPort) {
        this.repositorioInventarioPort = repositorioInventarioPort;
    }

    /**
     * Handle the query to get all inventory records.
     *
     * @param query the query to get all inventory records
     * @return the list of all inventory DTOs
     */
    public List<InventarioDto> handle(GetAllInventariosQuery query) {
        List<Inventario> inventarios = repositorioInventarioPort.findAll();
        return inventarios.stream()
                .map(InventarioMapper::toDto)
                .collect(Collectors.toList());
    }
}