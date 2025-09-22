package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.litethinking.supermercado.application.mapper.InventarioMapper;
import org.litethinking.supermercado.domain.model.inventario.Inventario;
import org.litethinking.supermercado.domain.ports.output.RepositorioInventarioPort;
import org.litethinking.supermercado.shareddto.cqrs.commands.UpdateInventarioCantidadCommand;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handler for UpdateInventarioCantidadCommand.
 * This class is responsible for processing the command to update the quantity of an existing inventory record.
 */
@Component
public class UpdateInventarioCantidadCommandHandler {

    private final RepositorioInventarioPort repositorioInventarioPort;

    public UpdateInventarioCantidadCommandHandler(RepositorioInventarioPort repositorioInventarioPort) {
        this.repositorioInventarioPort = repositorioInventarioPort;
    }

    /**
     * Handle the command to update the quantity of an existing inventory record.
     *
     * @param command the command to update the quantity of an inventory record
     * @return the updated inventory DTO
     * @throws IllegalArgumentException if the inventory with the given id is not found
     */
    public InventarioDto handle(UpdateInventarioCantidadCommand command) {
        Optional<Inventario> inventarioOptional = repositorioInventarioPort.findById(command.id());
        if (inventarioOptional.isEmpty()) {
            throw new IllegalArgumentException("Inventario no encontrado con ID: " + command.id());
        }

        Inventario inventario = inventarioOptional.get();
        inventario.setCantidad(command.cantidad());

        Inventario inventarioActualizado = repositorioInventarioPort.save(inventario);
        return InventarioMapper.toDto(inventarioActualizado);
    }
}