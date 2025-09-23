package org.litethinking.supermercado.application.service.impl;

import org.litethinking.supermercado.application.ServicioCliente;
import org.litethinking.supermercado.application.cqrs.handlers.commands.CreateClienteCommandHandler;
import org.litethinking.supermercado.application.cqrs.handlers.commands.DeleteClienteCommandHandler;
import org.litethinking.supermercado.application.cqrs.handlers.commands.UpdateClienteCommandHandler;
import org.litethinking.supermercado.application.cqrs.handlers.queries.GetAllClientesQueryHandler;
import org.litethinking.supermercado.application.cqrs.handlers.queries.GetClienteByEmailQueryHandler;
import org.litethinking.supermercado.application.cqrs.handlers.queries.GetClienteByIdQueryHandler;
import org.litethinking.supermercado.application.cqrs.handlers.queries.GetClientesByNombreContainingQueryHandler;
import org.litethinking.supermercado.shareddto.cqrs.commands.CreateClienteCommand;
import org.litethinking.supermercado.shareddto.cqrs.commands.DeleteClienteCommand;
import org.litethinking.supermercado.shareddto.cqrs.commands.UpdateClienteCommand;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetAllClientesQuery;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetClienteByEmailQuery;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetClienteByIdQuery;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetClientesByNombreContainingQuery;
import org.litethinking.supermercado.shareddto.supermercado.ClienteDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ServicioCliente using CQRS pattern.
 */
@Service
public class ServicioClienteImpl implements ServicioCliente {

    private final CreateClienteCommandHandler createClienteCommandHandler;
    private final UpdateClienteCommandHandler updateClienteCommandHandler;
    private final DeleteClienteCommandHandler deleteClienteCommandHandler;
    private final GetClienteByIdQueryHandler getClienteByIdQueryHandler;
    private final GetAllClientesQueryHandler getAllClientesQueryHandler;
    private final GetClienteByEmailQueryHandler getClienteByEmailQueryHandler;
    private final GetClientesByNombreContainingQueryHandler getClientesByNombreContainingQueryHandler;

    public ServicioClienteImpl(
            CreateClienteCommandHandler createClienteCommandHandler,
            UpdateClienteCommandHandler updateClienteCommandHandler,
            DeleteClienteCommandHandler deleteClienteCommandHandler,
            GetClienteByIdQueryHandler getClienteByIdQueryHandler,
            GetAllClientesQueryHandler getAllClientesQueryHandler,
            GetClienteByEmailQueryHandler getClienteByEmailQueryHandler,
            GetClientesByNombreContainingQueryHandler getClientesByNombreContainingQueryHandler) {
        this.createClienteCommandHandler = createClienteCommandHandler;
        this.updateClienteCommandHandler = updateClienteCommandHandler;
        this.deleteClienteCommandHandler = deleteClienteCommandHandler;
        this.getClienteByIdQueryHandler = getClienteByIdQueryHandler;
        this.getAllClientesQueryHandler = getAllClientesQueryHandler;
        this.getClienteByEmailQueryHandler = getClienteByEmailQueryHandler;
        this.getClientesByNombreContainingQueryHandler = getClientesByNombreContainingQueryHandler;
    }

    @Override
    public ClienteDto crearCliente(ClienteDto clienteDto) {
        CreateClienteCommand command = new CreateClienteCommand(
                clienteDto.nombre(),
                clienteDto.email(),
                clienteDto.telefono(),
                clienteDto.direccion()
        );
        return createClienteCommandHandler.handle(command);
    }

    @Override
    public ClienteDto actualizarCliente(Long id, ClienteDto clienteDto) {
        UpdateClienteCommand command = new UpdateClienteCommand(
                id,
                clienteDto.nombre(),
                clienteDto.email(),
                clienteDto.telefono(),
                clienteDto.direccion()
        );
        return updateClienteCommandHandler.handle(command);
    }

    @Override
    public Optional<ClienteDto> obtenerClientePorId(Long id) {
        GetClienteByIdQuery query = new GetClienteByIdQuery(id);
        return getClienteByIdQueryHandler.handle(query);
    }

    @Override
    public List<ClienteDto> obtenerTodosLosClientes() {
        GetAllClientesQuery query = new GetAllClientesQuery();
        return getAllClientesQueryHandler.handle(query);
    }

    @Override
    public void eliminarCliente(Long id) {
        DeleteClienteCommand command = new DeleteClienteCommand(id);
        deleteClienteCommandHandler.handle(command);
    }

    @Override
    public Optional<ClienteDto> obtenerClientePorEmail(String email) {
        GetClienteByEmailQuery query = new GetClienteByEmailQuery(email);
        return getClienteByEmailQueryHandler.handle(query);
    }

    @Override
    public List<ClienteDto> obtenerClientesPorNombreContaining(String nombre) {
        GetClientesByNombreContainingQuery query = new GetClientesByNombreContainingQuery(nombre);
        return getClientesByNombreContainingQueryHandler.handle(query);
    }
}
