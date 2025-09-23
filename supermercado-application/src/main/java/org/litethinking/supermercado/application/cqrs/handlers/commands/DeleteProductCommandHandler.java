package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.litethinking.supermercado.domain.ports.output.RepositorioProductoPort;
import org.litethinking.supermercado.shareddto.cqrs.commands.DeleteProductCommand;
import org.springframework.stereotype.Component;

/**
 * Handler for DeleteProductCommand.
 * This class is responsible for processing the command to delete an existing product.
 */
@Component
public class DeleteProductCommandHandler {

    private final RepositorioProductoPort repositorioProductoPort;

    public DeleteProductCommandHandler(RepositorioProductoPort repositorioProductoPort) {
        this.repositorioProductoPort = repositorioProductoPort;
    }

    /**
     * Handle the command to delete an existing product.
     *
     * @param command the command to delete a product
     */
    public void handle(DeleteProductCommand command) {
        repositorioProductoPort.deleteById(command.id());
    }
}