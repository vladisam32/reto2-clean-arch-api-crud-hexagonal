package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.litethinking.supermercado.application.mapper.ClienteMapper;
import org.litethinking.supermercado.domain.model.Cliente;
import org.litethinking.supermercado.domain.ports.output.RepositorioClientePort;
import org.litethinking.supermercado.shareddto.cqrs.commands.UpdateClienteCommand;
import org.litethinking.supermercado.shareddto.supermercado.ClienteDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handler for UpdateClienteCommand.
 * This class is responsible for processing the command to update an existing customer.
 */
@Component
public class UpdateClienteCommandHandler {

    private final RepositorioClientePort repositorioClientePort;

    public UpdateClienteCommandHandler(RepositorioClientePort repositorioClientePort) {
        this.repositorioClientePort = repositorioClientePort;
    }

    /**
     * Handle the command to update an existing customer.
     *
     * @param command the command to update a customer
     * @return the updated customer DTO
     * @throws IllegalArgumentException if the customer is not found
     */
    public ClienteDto handle(UpdateClienteCommand command) {
        Optional<Cliente> clienteExistente = repositorioClientePort.findById(command.id());
        if (clienteExistente.isPresent()) {
            Cliente cliente = new Cliente();
            cliente.setId(command.id());
            cliente.setNombre(command.nombre());
            cliente.setEmail(command.email());
            cliente.setTelefono(command.telefono());
            cliente.setDireccion(command.direccion());

            Cliente clienteActualizado = repositorioClientePort.save(cliente);
            return ClienteMapper.toDto(clienteActualizado);
        } else {
            throw new IllegalArgumentException("Cliente no encontrado con ID: " + command.id());
        }
    }
}