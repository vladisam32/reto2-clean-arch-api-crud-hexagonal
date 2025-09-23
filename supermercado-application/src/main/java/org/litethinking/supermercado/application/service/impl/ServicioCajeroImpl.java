package org.litethinking.supermercado.application.service.impl;

import org.litethinking.supermercado.application.ServicioCajero;
import org.litethinking.supermercado.application.cqrs.handlers.commands.CreateCajeroCommandHandler;
import org.litethinking.supermercado.application.cqrs.handlers.commands.DeleteCajeroCommandHandler;
import org.litethinking.supermercado.application.cqrs.handlers.commands.UpdateCajeroCommandHandler;
import org.litethinking.supermercado.application.cqrs.handlers.queries.GetAllCajerosQueryHandler;
import org.litethinking.supermercado.application.cqrs.handlers.queries.GetCajeroByCodigoQueryHandler;
import org.litethinking.supermercado.application.cqrs.handlers.queries.GetCajeroByIdQueryHandler;
import org.litethinking.supermercado.application.cqrs.handlers.queries.GetCajerosByTurnoQueryHandler;
import org.litethinking.supermercado.shareddto.cqrs.commands.CreateCajeroCommand;
import org.litethinking.supermercado.shareddto.cqrs.commands.DeleteCajeroCommand;
import org.litethinking.supermercado.shareddto.cqrs.commands.UpdateCajeroCommand;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetAllCajerosQuery;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetCajeroByCodigoQuery;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetCajeroByIdQuery;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetCajerosByTurnoQuery;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ServicioCajero using CQRS pattern.
 * This class is not a Spring bean, it's used internally by ServicioCajeroAdapter.
 */
@Service
public class ServicioCajeroImpl implements ServicioCajero {

    private final CreateCajeroCommandHandler createCajeroCommandHandler;
    private final UpdateCajeroCommandHandler updateCajeroCommandHandler;
    private final DeleteCajeroCommandHandler deleteCajeroCommandHandler;
    private final GetCajeroByIdQueryHandler getCajeroByIdQueryHandler;
    private final GetAllCajerosQueryHandler getAllCajerosQueryHandler;
    private final GetCajeroByCodigoQueryHandler getCajeroByCodigoQueryHandler;
    private final GetCajerosByTurnoQueryHandler getCajerosByTurnoQueryHandler;

    public ServicioCajeroImpl(
            CreateCajeroCommandHandler createCajeroCommandHandler,
            UpdateCajeroCommandHandler updateCajeroCommandHandler,
            DeleteCajeroCommandHandler deleteCajeroCommandHandler,
            GetCajeroByIdQueryHandler getCajeroByIdQueryHandler,
            GetAllCajerosQueryHandler getAllCajerosQueryHandler,
            GetCajeroByCodigoQueryHandler getCajeroByCodigoQueryHandler,
            GetCajerosByTurnoQueryHandler getCajerosByTurnoQueryHandler) {
        this.createCajeroCommandHandler = createCajeroCommandHandler;
        this.updateCajeroCommandHandler = updateCajeroCommandHandler;
        this.deleteCajeroCommandHandler = deleteCajeroCommandHandler;
        this.getCajeroByIdQueryHandler = getCajeroByIdQueryHandler;
        this.getAllCajerosQueryHandler = getAllCajerosQueryHandler;
        this.getCajeroByCodigoQueryHandler = getCajeroByCodigoQueryHandler;
        this.getCajerosByTurnoQueryHandler = getCajerosByTurnoQueryHandler;
    }

    @Override
    public CajeroDto crearCajero(CajeroDto cajeroDto) {
        CreateCajeroCommand command = new CreateCajeroCommand(
                cajeroDto.nombre(),
                cajeroDto.codigo(),
                cajeroDto.turno()
        );
        return createCajeroCommandHandler.handle(command);
    }

    @Override
    public CajeroDto actualizarCajero(Long id, CajeroDto cajeroDto) {
        UpdateCajeroCommand command = new UpdateCajeroCommand(
                id,
                cajeroDto.nombre(),
                cajeroDto.codigo(),
                cajeroDto.turno()
        );
        return updateCajeroCommandHandler.handle(command);
    }

    @Override
    public Optional<CajeroDto> obtenerCajeroPorId(Long id) {
        GetCajeroByIdQuery query = new GetCajeroByIdQuery(id);
        return getCajeroByIdQueryHandler.handle(query);
    }

    @Override
    public List<CajeroDto> obtenerTodosLosCajeros() {
        GetAllCajerosQuery query = new GetAllCajerosQuery();
        return getAllCajerosQueryHandler.handle(query);
    }

    @Override
    public void eliminarCajero(Long id) {
        DeleteCajeroCommand command = new DeleteCajeroCommand(id);
        deleteCajeroCommandHandler.handle(command);
    }

    @Override
    public Optional<CajeroDto> obtenerCajeroPorCodigo(String codigo) {
        GetCajeroByCodigoQuery query = new GetCajeroByCodigoQuery(codigo);
        return getCajeroByCodigoQueryHandler.handle(query);
    }

    @Override
    public List<CajeroDto> obtenerCajerosPorTurno(String turno) {
        GetCajerosByTurnoQuery query = new GetCajerosByTurnoQuery(turno);
        return getCajerosByTurnoQueryHandler.handle(query);
    }
}
