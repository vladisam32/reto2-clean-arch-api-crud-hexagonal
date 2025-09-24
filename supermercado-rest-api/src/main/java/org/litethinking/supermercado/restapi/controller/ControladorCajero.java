package org.litethinking.supermercado.restapi.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST pa' las operaciones de Cajero, ¡dale!
 * Implementado con patrón CQRS directamente en el controlador.
 */
@RestController
@RequestMapping("/api/cajeros")
@Slf4j
public class ControladorCajero {

    private final CreateCajeroCommandHandler createCajeroCommandHandler;
    private final UpdateCajeroCommandHandler updateCajeroCommandHandler;
    private final DeleteCajeroCommandHandler deleteCajeroCommandHandler;
    private final GetCajeroByIdQueryHandler getCajeroByIdQueryHandler;
    private final GetAllCajerosQueryHandler getAllCajerosQueryHandler;
    private final GetCajeroByCodigoQueryHandler getCajeroByCodigoQueryHandler;
    private final GetCajerosByTurnoQueryHandler getCajerosByTurnoQueryHandler;

    public ControladorCajero(
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
        log.info("Controlador de Cajero inicializao y listo pa' usarse");
    }

    /**
     * Crea un cajero nuevo, pa' que atienda la caja.
     * Usa directamente el patrón CQRS con CreateCajeroCommand.
     *
     * @param cajeroDto el cajero que vamo' a crear
     * @return el cajero ya creao'
     */
    @PostMapping
    public ResponseEntity<CajeroDto> crearCajero(@RequestBody CajeroDto cajeroDto) {
        log.info("Creando un cajero nuevo con nombre: {}", cajeroDto.nombre());

        CreateCajeroCommand command = new CreateCajeroCommand(
                cajeroDto.nombre(),
                cajeroDto.codigo(),
                cajeroDto.turno()
        );

        CajeroDto cajeroCreado = createCajeroCommandHandler.handle(command);
        log.debug("Cajero creao exitosamente con ID: {}", cajeroCreado.id());
        return new ResponseEntity<>(cajeroCreado, HttpStatus.CREATED);
    }

    /**
     * Actualiza un cajero que ya existe, pa' cambiarle los datos
     * Usa directamente el patrón CQRS con UpdateCajeroCommand.
     *
     * @param id el ID del cajero que vamo' a actualizar
     * @param cajeroDto los datos nuevos del cajero
     * @return el cajero ya actualizao
     */
    @PutMapping("/{id}")
    public ResponseEntity<CajeroDto> actualizarCajero(@PathVariable Long id, @RequestBody CajeroDto cajeroDto) {
        log.info("Actualizando el cajero con ID: {}", id);

        UpdateCajeroCommand command = new UpdateCajeroCommand(
                id,
                cajeroDto.nombre(),
                cajeroDto.codigo(),
                cajeroDto.turno()
        );

        CajeroDto cajeroActualizado = updateCajeroCommandHandler.handle(command);
        log.debug("Cajero actualizao exitosamente: {}", cajeroActualizado.nombre());
        return ResponseEntity.ok(cajeroActualizado);
    }

    /**
     * Busca un cajero por su ID, a ver si lo encontramo'.
     * Usa directamente el patrón CQRS con GetCajeroByIdQuery.
     *
     * @param id el ID del cajero que tamo' buscando
     * @return el cajero si aparece, si no un 404 pa' que sepa
     */
    @GetMapping("/{id}")
    public ResponseEntity<CajeroDto> obtenerCajeroPorId(@PathVariable Long id) {
        log.info("Buscando cajero con ID: {}", id);

        GetCajeroByIdQuery query = new GetCajeroByIdQuery(id);
        Optional<CajeroDto> cajero = getCajeroByIdQueryHandler.handle(query);

        if (cajero.isPresent()) {
            log.debug("Cajero encontrao: {}", cajero.get().nombre());
            return ResponseEntity.ok(cajero.get());
        } else {
            log.warn("No se encontró ningún cajero con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Trae toos los cajeros que hay en el sistema
     * Usa directamente el patrón CQRS con GetAllCajerosQuery.
     *
     * @return la lista completa de cajeros
     */
    @GetMapping
    public ResponseEntity<List<CajeroDto>> obtenerTodosLosCajeros() {
        log.info("Obteniendo toos los cajeros");

        GetAllCajerosQuery query = new GetAllCajerosQuery();
        List<CajeroDto> cajeros = getAllCajerosQueryHandler.handle(query);

        log.debug("Se encontraron {} cajeros", cajeros.size());
        return ResponseEntity.ok(cajeros);
    }

    /**
     * Elimina un cajero del sistema, pa' cuando ya no trabaje aquí
     * Usa directamente el patrón CQRS con DeleteCajeroCommand.
     *
     * @param id el ID del cajero que vamo' a eliminar
     * @return 204 si to' salió bien
     */
    @DeleteMapping("/{id}")
    @Hidden
    public ResponseEntity<Void> eliminarCajero(@PathVariable Long id) {
        log.info("Eliminando cajero con ID: {}", id);

        DeleteCajeroCommand command = new DeleteCajeroCommand(id);
        deleteCajeroCommandHandler.handle(command);

        log.debug("Cajero eliminao exitosamente");
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca un cajero por su código, ¡al toque!
     * Usa directamente el patrón CQRS con GetCajeroByCodigoQuery.
     *
     * @param codigo el código que tamo' buscando
     * @return el cajero si lo encontramo', si no un 404
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CajeroDto> obtenerCajeroPorCodigo(@PathVariable String codigo) {
        log.info("Buscando cajero con código: {}", codigo);

        GetCajeroByCodigoQuery query = new GetCajeroByCodigoQuery(codigo);
        Optional<CajeroDto> cajero = getCajeroByCodigoQueryHandler.handle(query);

        if (cajero.isPresent()) {
            log.debug("Cajero encontrao por código: {}", cajero.get().nombre());
            return ResponseEntity.ok(cajero.get());
        } else {
            log.warn("No se encontró ningún cajero con código: {}", codigo);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca toos los cajeros de un turno específico
     * Usa directamente el patrón CQRS con GetCajerosByTurnoQuery.
     *
     * @param turno el turno que queremo filtrar
     * @return la lista de cajeros de ese turno
     */
    @GetMapping("/turno/{turno}")
    public ResponseEntity<List<CajeroDto>> obtenerCajerosPorTurno(@PathVariable String turno) {
        log.info("Buscando cajeros del turno: {}", turno);

        GetCajerosByTurnoQuery query = new GetCajerosByTurnoQuery(turno);
        List<CajeroDto> cajeros = getCajerosByTurnoQueryHandler.handle(query);

        log.debug("Se encontraron {} cajeros en el turno {}", cajeros.size(), turno);
        return ResponseEntity.ok(cajeros);
    }
}
