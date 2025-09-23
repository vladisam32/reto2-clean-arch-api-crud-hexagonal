package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.litethinking.supermercado.application.mapper.InventarioMapper;
import org.litethinking.supermercado.application.mapper.ProductoMapper;
import org.litethinking.supermercado.domain.model.inventario.Inventario;
import org.litethinking.supermercado.domain.ports.output.RepositorioInventarioPort;
import org.litethinking.supermercado.shareddto.cqrs.commands.CreateInventarioCommand;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.springframework.stereotype.Component;

/**
 * Handler for CreateInventarioCommand.
 * This class is responsible for processing the command to create a new inventory record.
 */
@Component
public class CreateInventarioCommandHandler {

    private final RepositorioInventarioPort repositorioInventarioPort;

    public CreateInventarioCommandHandler(RepositorioInventarioPort repositorioInventarioPort) {
        this.repositorioInventarioPort = repositorioInventarioPort;
    }

    /**
     * Handle the command to create a new inventory record.
     *
     * @param command the command to create an inventory record
     * @return the created inventory DTO
     */
    public InventarioDto handle(CreateInventarioCommand command) {
        Inventario inventario = Inventario.builder()
                .producto(ProductoMapper.toDomain(command.producto()))
                .cantidad(command.cantidad())
                .stockMinimo(command.stockMinimo())
                .stockMaximo(command.stockMaximo())
                .fechaUltimaReposicion(command.fechaUltimaReposicion())
                .ubicacion(command.ubicacion())
                .build();

        Inventario inventarioGuardado = repositorioInventarioPort.save(inventario);
        return InventarioMapper.toDto(inventarioGuardado);
    }
}