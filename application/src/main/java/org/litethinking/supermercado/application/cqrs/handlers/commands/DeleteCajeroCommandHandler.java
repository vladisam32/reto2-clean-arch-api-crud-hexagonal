package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.litethinking.supermercado.domain.ports.output.RepositorioCajeroPort;
import org.litethinking.supermercado.shareddto.cqrs.commands.DeleteCajeroCommand;
import org.springframework.stereotype.Component;

/**
 * Handler for DeleteCajeroCommand.
 * This class is responsible for processing the command to delete a cashier.
 */
@Component
public class DeleteCajeroCommandHandler {

    private final RepositorioCajeroPort repositorioCajeroPort;

    public DeleteCajeroCommandHandler(RepositorioCajeroPort repositorioCajeroPort) {
        this.repositorioCajeroPort = repositorioCajeroPort;
    }

    /**
     * Handle the command to delete a cashier.
     *
     * @param command the command to delete a cashier
     */
    public void handle(DeleteCajeroCommand command) {
        repositorioCajeroPort.deleteById(command.id());
    }
}
