package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.InventarioMapper;
import org.litethinking.supermercado.domain.model.inventario.Inventario;
import org.litethinking.supermercado.domain.repository.inventario.RepositorioInventario;
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

    private final RepositorioInventario repositorioInventario;

    public GetAllInventariosQueryHandler(RepositorioInventario repositorioInventario) {
        this.repositorioInventario = repositorioInventario;
    }

    /**
     * Handle the query to get all inventory records.
     *
     * @param query the query to get all inventory records
     * @return the list of all inventory DTOs
     */
    public List<InventarioDto> handle(GetAllInventariosQuery query) {
        List<Inventario> inventarios = repositorioInventario.findAll();
        return inventarios.stream()
                .map(InventarioMapper::toDto)
                .collect(Collectors.toList());
    }
}