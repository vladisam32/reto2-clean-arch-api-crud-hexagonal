package org.litethinking.supermercado.restapi.controller.inventario;

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
 * REST controller for Inventario operations.
 */
@RestController
@RequestMapping("/api/inventario")
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
     * Create a new inventory record.
     *
     * @param command the command to create an inventory
     * @return the created inventory
     */
    @PostMapping
    public ResponseEntity<InventarioDto> crearInventario(@RequestBody CreateInventarioCommand command) {
        InventarioDto inventarioCreado = createInventarioCommandHandler.handle(command);
        return new ResponseEntity<>(inventarioCreado, HttpStatus.CREATED);
    }

    /**
     * Update an existing inventory record.
     *
     * @param id the id of the inventory to update
     * @param command the command to update an inventory
     * @return the updated inventory
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventarioDto> actualizarInventario(@PathVariable Long id, @RequestBody UpdateInventarioCommand command) {
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
     * Get an inventory record by its id.
     *
     * @param id the id of the inventory
     * @return the inventory if found, 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventarioDto> obtenerInventarioPorId(@PathVariable Long id) {
        GetInventarioByIdQuery query = new GetInventarioByIdQuery(id);
        Optional<InventarioDto> inventario = getInventarioByIdQueryHandler.handle(query);
        return inventario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all inventory records.
     *
     * @return the list of all inventory records
     */
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
