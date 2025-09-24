package org.litethinking.supermercado.restapi.controller.inventario;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.litethinking.supermercado.application.cqrs.handlers.commands.*;
import org.litethinking.supermercado.application.cqrs.handlers.queries.*;
import org.litethinking.supermercado.shareddto.cqrs.commands.*;
import org.litethinking.supermercado.shareddto.cqrs.queries.*;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST pa' las operaciones de Inventario, ¡con to'!
 */
@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "API pa' manejar to' el inventario del supermercado, ¡tú 'ta claro!")
public class ControladorInventario {

    private final CreateInventarioCommandHandler createInventarioCommandHandler;
    private final UpdateInventarioCommandHandler updateInventarioCommandHandler;
    private final DeleteInventarioCommandHandler deleteInventarioCommandHandler;
    private final UpdateInventarioCantidadCommandHandler updateInventarioCantidadCommandHandler;
    private final GetInventarioByIdQueryHandler getInventarioByIdQueryHandler;
    private final GetAllInventariosQueryHandler getAllInventariosQueryHandler;
    private final GetInventarioByProductoIdQueryHandler getInventarioByProductoIdQueryHandler;
    private final GetInventariosConBajoStockQueryHandler getInventariosConBajoStockQueryHandler;
    private final GetInventariosByUbicacionQueryHandler getInventariosByUbicacionQueryHandler;

    public ControladorInventario(
            CreateInventarioCommandHandler createInventarioCommandHandler,
            UpdateInventarioCommandHandler updateInventarioCommandHandler,
            DeleteInventarioCommandHandler deleteInventarioCommandHandler,
            UpdateInventarioCantidadCommandHandler updateInventarioCantidadCommandHandler,
            GetInventarioByIdQueryHandler getInventarioByIdQueryHandler,
            GetAllInventariosQueryHandler getAllInventariosQueryHandler,
            GetInventarioByProductoIdQueryHandler getInventarioByProductoIdQueryHandler,
            GetInventariosConBajoStockQueryHandler getInventariosConBajoStockQueryHandler,
            GetInventariosByUbicacionQueryHandler getInventariosByUbicacionQueryHandler) {
        this.createInventarioCommandHandler = createInventarioCommandHandler;
        this.updateInventarioCommandHandler = updateInventarioCommandHandler;
        this.deleteInventarioCommandHandler = deleteInventarioCommandHandler;
        this.updateInventarioCantidadCommandHandler = updateInventarioCantidadCommandHandler;
        this.getInventarioByIdQueryHandler = getInventarioByIdQueryHandler;
        this.getAllInventariosQueryHandler = getAllInventariosQueryHandler;
        this.getInventarioByProductoIdQueryHandler = getInventarioByProductoIdQueryHandler;
        this.getInventariosConBajoStockQueryHandler = getInventariosConBajoStockQueryHandler;
        this.getInventariosByUbicacionQueryHandler = getInventariosByUbicacionQueryHandler;
    }

    /**
     * Crea un nuevo registro de inventario, ¡pa' tener to' controladito!
     *
     * @param command el comando pa' crear un inventario
     * @return el inventario ya creao'
     */
    @Operation(
        summary = "Crear un inventario nuevecito",
        description = "Mete un registro de inventario nuevo en el sistema con to' la información que le dimo'"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "¡Inventario creao' exitosamente, manín!",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InventarioDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Los datos del inventario 'tán malos, arréglalo",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Se rompió el servidor, ¡qué lío!",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<InventarioDto> crearInventario(@Parameter(description = "Datos del inventario a crear", required = true) @RequestBody CreateInventarioCommand command) {
        InventarioDto inventarioCreado = createInventarioCommandHandler.handle(command);
        return new ResponseEntity<>(inventarioCreado, HttpStatus.CREATED);
    }

    /**
     * Actualiza un inventario que ya ta' en el sistema.
     *
     * @param id el ID del inventario que vamo' a actualizar
     * @param command el comando con los datos nuevecitos del inventario
     * @return el inventario ya actualizao'
     */
    @Operation(
        summary = "Actualizar un inventario, ¡al toque!",
        description = "Cambia la información de un inventario que ya 'tá en el sistema, tú sabe"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡Inventario actualizao' exitosamente, qué bueno!",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InventarioDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "No encontramo' ese inventario, ¿seguro existe?",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Esos datos 'tán malos, revísalos bien",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "El servidor se volvió loco, ¡qué vaina!",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<InventarioDto> actualizarInventario(
            @Parameter(description = "ID del inventario a actualizar", required = true) @PathVariable Long id, 
            @Parameter(description = "Datos actualizados del inventario", required = true) @RequestBody UpdateInventarioCommand command) {
        // Ensure the ID in the path matches the ID in the command
        UpdateInventarioCommand commandWithId = new UpdateInventarioCommand(
            id,
            command.producto(),
            command.cantidad(),
            command.stockMinimo(),
            command.stockMaximo(),
            command.fechaUltimaReposicion(),
            command.ubicacion()
        );
        InventarioDto inventarioActualizado = updateInventarioCommandHandler.handle(commandWithId);
        return ResponseEntity.ok(inventarioActualizado);
    }

    /**
     * Busca un inventario por su ID, ¿tú 'ta claro?
     *
     * @param id el ID del inventario que queremo' encontrar
     * @return el inventario si aparece, si no un 404 pa' que sepa
     */
    @Operation(
        summary = "Buscar un inventario por su ID, ¡rapidito!",
        description = "Busca y te trae un inventario por el ID que le dimo'"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡Lo encontramo'! Aquí 'tá tu inventario",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InventarioDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "No hay na' con ese ID, búscate otro",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "El servidor explotó, ¡qué desastre!",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventarioDto> obtenerInventarioPorId(@Parameter(description = "ID del inventario a buscar", required = true) @PathVariable Long id) {
        GetInventarioByIdQuery query = new GetInventarioByIdQuery(id);
        Optional<InventarioDto> inventario = getInventarioByIdQueryHandler.handle(query);
        return inventario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Trae to' el inventario que hay, ¡completo!
     *
     * @return la lista con to' el inventario
     */
    @Operation(
        summary = "Traer to' el inventario, ¡completo!",
        description = "Te busca la lista completa de to' el inventario que hay en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡Ahí te va to' el inventario, manín!",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InventarioDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "El servidor se fue a dormir, ¡despiértalo!",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<List<InventarioDto>> obtenerTodoElInventario() {
        GetAllInventariosQuery query = new GetAllInventariosQuery();
        List<InventarioDto> listaInventario = getAllInventariosQueryHandler.handle(query);
        return ResponseEntity.ok(listaInventario);
    }

    /**
     * Delete an inventory record by its id.
     *
     * @param id the id of the inventory to delete
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    @Hidden
    public ResponseEntity<Void> eliminarInventario(@PathVariable Long id) {
        DeleteInventarioCommand command = new DeleteInventarioCommand(id);
        deleteInventarioCommandHandler.handle(command);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get inventory record by product.
     *
     * @param productoId the id of the product to search for
     * @return the inventory if found, 404 otherwise
     */
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<InventarioDto> obtenerInventarioPorProducto(@PathVariable Long productoId) {
        GetInventarioByProductoIdQuery query = new GetInventarioByProductoIdQuery(productoId);
        Optional<InventarioDto> inventario = getInventarioByProductoIdQueryHandler.handle(query);
        return inventario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get inventory records with low stock.
     *
     * @return the list of inventory records with low stock
     */
    @GetMapping("/bajo-stock")
    public ResponseEntity<List<InventarioDto>> obtenerInventarioConBajoStock() {
        GetInventariosConBajoStockQuery query = new GetInventariosConBajoStockQuery();
        List<InventarioDto> listaInventario = getInventariosConBajoStockQueryHandler.handle(query);
        return ResponseEntity.ok(listaInventario);
    }

    /**
     * Get inventory records by location.
     *
     * @param ubicacion the location to search for
     * @return the list of inventory records in the location
     */
    @GetMapping("/ubicacion/{ubicacion}")
    public ResponseEntity<List<InventarioDto>> obtenerInventarioPorUbicacion(@PathVariable String ubicacion) {
        GetInventariosByUbicacionQuery query = new GetInventariosByUbicacionQuery(ubicacion);
        List<InventarioDto> listaInventario = getInventariosByUbicacionQueryHandler.handle(query);
        return ResponseEntity.ok(listaInventario);
    }

    /**
     * Update inventory quantity.
     *
     * @param id the id of the inventory to update
     * @param cantidad the new quantity
     * @return the updated inventory
     */
    @PatchMapping("/{id}/cantidad/{cantidad}")
    public ResponseEntity<InventarioDto> actualizarCantidadInventario(@PathVariable Long id, @PathVariable Integer cantidad) {
        UpdateInventarioCantidadCommand command = new UpdateInventarioCantidadCommand(id, cantidad);
        InventarioDto inventarioActualizado = updateInventarioCantidadCommandHandler.handle(command);
        return ResponseEntity.ok(inventarioActualizado);
    }

    /**
     * Genera un archivo CSV con todo el inventario.
     *
     * @return un archivo CSV con todo el inventario
     */
    @GetMapping("/csv")
    public ResponseEntity<String> generarCsvInventario() {
        GetAllInventariosQuery query = new GetAllInventariosQuery();
        List<InventarioDto> inventarios = getAllInventariosQueryHandler.handle(query);

        // Crear el contenido del CSV
        StringBuilder csv = new StringBuilder();
        csv.append("id,producto_id,cantidad,stockMinimo,stockMaximo,fechaUltimaReposicion,ubicacion\n");

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        for (InventarioDto inventario : inventarios) {
            csv.append(inventario.id()).append(",")
               .append(inventario.producto().id()).append(",")
               .append(inventario.cantidad()).append(",")
               .append(inventario.stockMinimo()).append(",")
               .append(inventario.stockMaximo()).append(",")
               .append(inventario.fechaUltimaReposicion().format(formatter)).append(",")
               .append(inventario.ubicacion()).append("\n");
        }

        // Configurar las cabeceras para la descarga del archivo
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "inventario.csv");

        return new ResponseEntity<>(csv.toString(), headers, HttpStatus.OK);
    }
}
