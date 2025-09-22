package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.litethinking.supermercado.application.mapper.CajeroMapper;
import org.litethinking.supermercado.domain.model.Cajero;
import org.litethinking.supermercado.domain.ports.output.RepositorioCajeroPort;
///import org.litethinking.supermercado.domain.repository.RepositorioCajero;
import org.litethinking.supermercado.shareddto.cqrs.commands.CreateCajeroCommand;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.springframework.stereotype.Component;

/**
 * Handler for CreateCajeroCommand.
 * This class is responsible for processing the command to create a new cashier.
 */
@Component
public class CreateCajeroCommandHandler {

    private final RepositorioCajeroPort repositorioCajeroPort;

    public CreateCajeroCommandHandler(RepositorioCajeroPort repositorioCajeroPort) {
        this.repositorioCajeroPort =  repositorioCajeroPort;
    }

    /**
     * Handle the command to create a new cashier.
     *
     * @param command the command to create a cashier
     * @return the created cashier DTO
     */
    public CajeroDto handle(CreateCajeroCommand command) {
        Cajero cajero = new Cajero();
        cajero.setNombre(command.nombre());
        cajero.setCodigo(command.codigo());
        cajero.setTurno(command.turno());

        Cajero cajeroGuardado = repositorioCajeroPort.save(cajero);
        return CajeroMapper.toDto(cajeroGuardado);
    }
}
