package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.litethinking.supermercado.domain.ports.output.RepositorioClientePort;
import org.litethinking.supermercado.shareddto.cqrs.commands.DeleteClienteCommand;
import org.springframework.stereotype.Component;

/**
 * Handler for DeleteClienteCommand.
 * This class is responsible for processing the command to delete a customer.
 */
@Component
public class DeleteClienteCommandHandler {

    private final RepositorioClientePort repositorioClientePort;

    public DeleteClienteCommandHandler(RepositorioClientePort repositorioClientePort) {
        this.repositorioClientePort = repositorioClientePort;
    }

    /**
     * Handle the command to delete a customer.
     *
     * @param command the command to delete a customer
     */
    public void handle(DeleteClienteCommand command) {
        repositorioClientePort.deleteById(command.id());
    }
}