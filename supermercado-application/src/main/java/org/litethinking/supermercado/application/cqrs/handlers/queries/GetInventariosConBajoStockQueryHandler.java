package org.litethinking.supermercado.application.cqrs.handlers.queries;

import org.litethinking.supermercado.application.mapper.InventarioMapper;
import org.litethinking.supermercado.domain.model.inventario.Inventario;
import org.litethinking.supermercado.domain.ports.output.RepositorioInventarioPort;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetInventariosConBajoStockQuery;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handler for GetInventariosConBajoStockQuery.
 * This class is responsible for processing the query to get inventory records with low stock.
 */
@Component
public class GetInventariosConBajoStockQueryHandler {

    private final RepositorioInventarioPort repositorioInventarioPort;

    public GetInventariosConBajoStockQueryHandler(RepositorioInventarioPort repositorioInventarioPort) {
        this.repositorioInventarioPort = repositorioInventarioPort;
    }

    /**
     * Handle the query to get inventory records with low stock.
     *
     * @param query the query to get inventory records with low stock
     * @return the list of inventory DTOs with low stock
     */
    public List<InventarioDto> handle(GetInventariosConBajoStockQuery query) {
        List<Inventario> inventarios = repositorioInventarioPort.findBajoStock();
        return inventarios.stream()
                .map(InventarioMapper::toDto)
                .collect(Collectors.toList());
    }
}
