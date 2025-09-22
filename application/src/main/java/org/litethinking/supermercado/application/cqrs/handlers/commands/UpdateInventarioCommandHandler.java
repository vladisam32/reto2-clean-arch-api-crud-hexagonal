package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.litethinking.supermercado.application.mapper.InventarioMapper;
import org.litethinking.supermercado.application.mapper.ProductoMapper;
import org.litethinking.supermercado.domain.model.inventario.Inventario;
import org.litethinking.supermercado.domain.ports.output.RepositorioInventarioPort;
import org.litethinking.supermercado.shareddto.cqrs.commands.UpdateInventarioCommand;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handler for UpdateInventarioCommand.
 * This class is responsible for processing the command to update an existing inventory record.
 */
@Component
public class UpdateInventarioCommandHandler {

    private final RepositorioInventarioPort repositorioInventarioPort;

    public UpdateInventarioCommandHandler(RepositorioInventarioPort repositorioInventarioPort) {
        this.repositorioInventarioPort = repositorioInventarioPort;
    }

    /**
     * Handle the command to update an existing inventory record.
     *
     * @param command the command to update an inventory record
     * @return the updated inventory DTO
     * @throws IllegalArgumentException if the inventory with the given id is not found
     */
    public InventarioDto handle(UpdateInventarioCommand command) {
        Optional<Inventario> inventarioOptional = repositorioInventarioPort.findById(command.id());
        if (inventarioOptional.isEmpty()) {
            throw new IllegalArgumentException("Inventario no encontrado con ID: " + command.id());
        }

        Inventario inventario = inventarioOptional.get();
        inventario.setProducto(ProductoMapper.toDomain(command.producto()));
        inventario.setCantidad(command.cantidad());
        inventario.setStockMinimo(command.stockMinimo());
        inventario.setStockMaximo(command.stockMaximo());
        inventario.setFechaUltimaReposicion(command.fechaUltimaReposicion());
        inventario.setUbicacion(command.ubicacion());

        Inventario inventarioActualizado = repositorioInventarioPort.save(inventario);
        return InventarioMapper.toDto(inventarioActualizado);
    }
}