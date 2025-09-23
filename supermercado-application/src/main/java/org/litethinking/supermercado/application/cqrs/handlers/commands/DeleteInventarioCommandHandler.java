package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.litethinking.supermercado.domain.ports.output.RepositorioInventarioPort;
import org.litethinking.supermercado.shareddto.cqrs.commands.DeleteInventarioCommand;
import org.springframework.stereotype.Component;

/**
 * Handler for DeleteInventarioCommand.
 * This class is responsible for processing the command to delete an existing inventory record.
 */
@Component
public class DeleteInventarioCommandHandler {

    private final RepositorioInventarioPort repositorioInventarioPort;

    public DeleteInventarioCommandHandler(RepositorioInventarioPort repositorioInventarioPort) {
        this.repositorioInventarioPort = repositorioInventarioPort;
    }

    /**
     * Handle the command to delete an existing inventory record.
     *
     * @param command the command to delete an inventory record
     */
    public void handle(DeleteInventarioCommand command) {
        repositorioInventarioPort.deleteById(command.id());
    }
}