package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.litethinking.supermercado.application.mapper.CajeroMapper;
import org.litethinking.supermercado.domain.model.Cajero;
import org.litethinking.supermercado.domain.ports.output.RepositorioCajeroPort;
import org.litethinking.supermercado.shareddto.cqrs.commands.UpdateCajeroCommand;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handler for UpdateCajeroCommand.
 * This class is responsible for processing the command to update an existing cashier.
 */
@Component
public class UpdateCajeroCommandHandler {

    private final RepositorioCajeroPort repositorioCajeroPort;

    public UpdateCajeroCommandHandler(RepositorioCajeroPort repositorioCajeroPort) {
        this.repositorioCajeroPort = repositorioCajeroPort;
    }

    /**
     * Handle the command to update an existing cashier.
     *
     * @param command the command to update a cashier
     * @return the updated cashier DTO
     * @throws IllegalArgumentException if the cashier is not found
     */
    public CajeroDto handle(UpdateCajeroCommand command) {
        Optional<Cajero> cajeroExistente = repositorioCajeroPort.findById(command.id());
        if (cajeroExistente.isPresent()) {
            Cajero cajero = new Cajero();
            cajero.setId(command.id());
            cajero.setNombre(command.nombre());
            cajero.setCodigo(command.codigo());
            cajero.setTurno(command.turno());

            Cajero cajeroActualizado = repositorioCajeroPort.save(cajero);
            return CajeroMapper.toDto(cajeroActualizado);
        } else {
            throw new IllegalArgumentException("Cajero no encontrado con ID: " + command.id());
        }
    }
}
