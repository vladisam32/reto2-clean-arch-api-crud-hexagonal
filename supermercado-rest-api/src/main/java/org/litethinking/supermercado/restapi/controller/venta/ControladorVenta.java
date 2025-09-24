package org.litethinking.supermercado.restapi.controller.venta;

import io.swagger.v3.oas.annotations.Hidden;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.litethinking.supermercado.application.service.venta.ServicioVenta;
import org.litethinking.supermercado.shareddto.supermercado.venta.VentaDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Sale operations.
 */
@RestController
@RequestMapping("/api/ventas")
public class ControladorVenta {

    private static final Logger logger = LogManager.getLogger(ControladorVenta.class);
    private final ServicioVenta servicioVenta;

    public ControladorVenta(ServicioVenta servicioVenta) {
        this.servicioVenta = servicioVenta;
        logger.info("Sale Controller initialized and ready to work");
    }

    /**
     * Creates a new sale.
     *
     * @param ventaDto the sale to create
     * @return the created sale
     */
    @PostMapping
    public ResponseEntity<VentaDto> crearVenta(@RequestBody VentaDto ventaDto) {
        logger.info("Creating new sale for customer: {}", ventaDto.nombreCliente());
        try {
            VentaDto ventaCreada = servicioVenta.crearVenta(ventaDto);
            logger.info("Sale created successfully! ID: {}, Customer: {}, Total: ${}", 
                ventaCreada.id(), ventaCreada.nombreCliente(), ventaCreada.montoTotal());
            return new ResponseEntity<>(ventaCreada, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating sale for customer: {}", ventaDto.nombreCliente(), e);
            throw e;
        }
    }

    /**
     * Updates an existing sale.
     *
     * @param id the ID of the sale to update
     * @param ventaDto the updated sale data
     * @return the updated sale
     */
    @PutMapping("/{id}")
    public ResponseEntity<VentaDto> actualizarVenta(@PathVariable Long id, @RequestBody VentaDto ventaDto) {
        logger.info("Updating sale with ID: {}, Customer: {}", id, ventaDto.nombreCliente());
        try {
            VentaDto ventaActualizada = servicioVenta.actualizarVenta(id, ventaDto);
            logger.info("Sale updated successfully! ID: {}, Customer: {}, New Total: ${}", 
                ventaActualizada.id(), ventaActualizada.nombreCliente(), ventaActualizada.montoTotal());
            return ResponseEntity.ok(ventaActualizada);
        } catch (Exception e) {
            logger.error("Error updating sale with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Gets a sale by its ID.
     *
     * @param id the ID of the sale to find
     * @return the sale if found, 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<VentaDto> obtenerVentaPorId(@PathVariable Long id) {
        logger.debug("Finding sale with ID: {}", id);
        try {
            Optional<VentaDto> venta = servicioVenta.obtenerVentaPorId(id);
            if (venta.isPresent()) {
                logger.info("Sale found! ID: {}, Customer: {}, Total: ${}", 
                    id, venta.get().nombreCliente(), venta.get().montoTotal());
                return ResponseEntity.ok(venta.get());
            } else {
                logger.warn("No sale found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error finding sale with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Gets all sales.
     *
     * @return the list of all sales
     */
    @GetMapping
    public ResponseEntity<List<VentaDto>> obtenerTodasLasVentas() {
        logger.debug("Finding all sales");
        try {
            List<VentaDto> ventas = servicioVenta.obtenerTodasLasVentas();
            logger.info("Found {} sales in total", ventas.size());

            // If there are few sales, we can show more details at debug level
            if (logger.isDebugEnabled() && ventas.size() < 10) {
                for (VentaDto venta : ventas) {
                    logger.debug("Sale ID: {}, Customer: {}, Total: ${}, Date: {}", 
                        venta.id(), venta.nombreCliente(), 
                        venta.montoTotal(), venta.fechaVenta());
                }
            }

            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            logger.error("Error retrieving the list of sales", e);
            throw e;
        }
    }

    /**
     * Deletes a sale from the system.
     *
     * @param id the ID of the sale to delete
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    @Hidden
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        logger.info("Deleting sale with ID: {}", id);
        try {
            // Optionally, we could get the sale first to log more details
            Optional<VentaDto> venta = servicioVenta.obtenerVentaPorId(id);
            if (venta.isPresent()) {
                logger.debug("Deleting sale: ID={}, Customer={}, Total=${}", 
                    id, venta.get().nombreCliente(), venta.get().montoTotal());
            }

            servicioVenta.eliminarVenta(id);
            logger.info("Sale deleted successfully! ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting sale with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Gets sales by customer name.
     *
     * @param nombreCliente the customer name to search for
     * @return the list of sales for the customer
     */
    @GetMapping("/cliente/{nombreCliente}")
    public ResponseEntity<List<VentaDto>> obtenerVentasPorNombreCliente(@PathVariable String nombreCliente) {
        logger.info("Finding sales for customer: {}", nombreCliente);
        try {
            List<VentaDto> ventas = servicioVenta.obtenerVentasPorNombreCliente(nombreCliente);
            logger.info("Found {} sales for customer: {}", ventas.size(), nombreCliente);

            // Log sale details at debug level
            if (logger.isDebugEnabled() && !ventas.isEmpty()) {
                for (VentaDto venta : ventas) {
                    logger.debug("Sale ID: {}, Customer: {}, Total: ${}, Date: {}", 
                        venta.id(), nombreCliente, venta.montoTotal(), venta.fechaVenta());
                }
            }

            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            logger.error("Error finding sales for customer: {}", nombreCliente, e);
            throw e;
        }
    }

    /**
     * Gets sales between two dates.
     *
     * @param fechaInicio the start date
     * @param fechaFin the end date
     * @return the list of sales between the dates
     */
    @GetMapping("/fechas")
    public ResponseEntity<List<VentaDto>> obtenerVentasEntreFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        logger.info("Finding sales between {} and {}", fechaInicio, fechaFin);
        try {
            List<VentaDto> ventas = servicioVenta.obtenerVentasEntreFechas(fechaInicio, fechaFin);
            logger.info("Found {} sales between those dates", ventas.size());

            // Calculate the total sold to show in the log
            if (!ventas.isEmpty()) {
                double totalVendido = ventas.stream()
                    .mapToDouble(v -> v.montoTotal().doubleValue())
                    .sum();
                logger.info("Total sold in that period: ${}", totalVendido);

                // Log sale IDs at debug level
                if (logger.isDebugEnabled()) {
                    String ventasIds = ventas.stream()
                        .map(v -> v.id().toString())
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("");
                    logger.debug("Sale IDs found: {}", ventasIds);
                }
            }

            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            logger.error("Error finding sales between {} and {}", fechaInicio, fechaFin, e);
            throw e;
        }
    }

    /**
     * Gets sales by payment method.
     *
     * @param metodoPago the payment method to search for
     * @return the list of sales with the payment method
     */
    @GetMapping("/metodo-pago/{metodoPago}")
    public ResponseEntity<List<VentaDto>> obtenerVentasPorMetodoPago(@PathVariable String metodoPago) {
        logger.info("Finding sales with payment method: {}", metodoPago);
        try {
            List<VentaDto> ventas = servicioVenta.obtenerVentasPorMetodoPago(metodoPago);
            logger.info("Found {} sales with payment method '{}'", ventas.size(), metodoPago);

            // Calculate the total sold with this payment method
            if (!ventas.isEmpty()) {
                double totalVendido = ventas.stream()
                    .mapToDouble(v -> v.montoTotal().doubleValue())
                    .sum();
                logger.info("Total sold with {}: ${}", metodoPago, totalVendido);

                // Log sale details at debug level
                if (logger.isDebugEnabled()) {
                    for (VentaDto venta : ventas) {
                        logger.debug("Sale ID: {}, Customer: {}, Total: ${}, Date: {}", 
                            venta.id(), venta.nombreCliente(), 
                            venta.montoTotal(), venta.fechaVenta());
                    }
                }
            }

            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            logger.error("Error finding sales with payment method: {}", metodoPago, e);
            throw e;
        }
    }
}
