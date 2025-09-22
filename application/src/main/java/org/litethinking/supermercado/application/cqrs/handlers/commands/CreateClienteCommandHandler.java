package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.litethinking.supermercado.application.mapper.ClienteMapper;
import org.litethinking.supermercado.domain.model.Cliente;
import org.litethinking.supermercado.domain.ports.output.RepositorioClientePort;
import org.litethinking.supermercado.shareddto.cqrs.commands.CreateClienteCommand;
import org.litethinking.supermercado.shareddto.supermercado.ClienteDto;
import org.springframework.stereotype.Component;

/**
 * Handler for CreateClienteCommand.
 * This class is responsible for processing the command to create a new customer.
 */
@Component
public class CreateClienteCommandHandler {

    private final RepositorioClientePort repositorioClientePort;

    public CreateClienteCommandHandler(RepositorioClientePort repositorioClientePort) {
        this.repositorioClientePort = repositorioClientePort;
    }

    /**
     * Handle the command to create a new customer.
     *
     * @param command the command to create a customer
     * @return the created customer DTO
     */
    public ClienteDto handle(CreateClienteCommand command) {
        Cliente cliente = new Cliente();
        cliente.setNombre(command.nombre());
        cliente.setEmail(command.email());
        cliente.setTelefono(command.telefono());
        cliente.setDireccion(command.direccion());

        Cliente clienteGuardado = repositorioClientePort.save(cliente);
        return ClienteMapper.toDto(clienteGuardado);
    }
}