package org.litethinking.supermercado.application.service.venta;

import org.litethinking.supermercado.shareddto.supermercado.venta.VentaDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Venta operations.
 */
public interface ServicioVenta {

    /**
     * Create a new sale.
     *
     * @param ventaDto the sale to create
     * @return the created sale
     */
    VentaDto crearVenta(VentaDto ventaDto);

    /**
     * Update an existing sale.
     *
     * @param id the id of the sale to update
     * @param ventaDto the updated sale data
     * @return the updated sale
     */
    VentaDto actualizarVenta(Long id, VentaDto ventaDto);

    /**
     * Get a sale by its id.
     *
     * @param id the id of the sale
     * @return the sale if found, empty otherwise
     */
    Optional<VentaDto> obtenerVentaPorId(Long id);

    /**
     * Get all sales.
     *
     * @return the list of all sales
     */
    List<VentaDto> obtenerTodasLasVentas();

    /**
     * Delete a sale by its id.
     *
     * @param id the id of the sale to delete
     */
    void eliminarVenta(Long id);

    /**
     * Get sales by customer name.
     *
     * @param nombreCliente the customer name to search for
     * @return the list of sales for the customer
     */
    List<VentaDto> obtenerVentasPorNombreCliente(String nombreCliente);

    /**
     * Get sales between dates.
     *
     * @param fechaInicio the start date
     * @param fechaFin the end date
     * @return the list of sales between the dates
     */
    List<VentaDto> obtenerVentasEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Get sales by total amount greater than.
     *
     * @param montoMinimo the minimum total amount
     * @return the list of sales with total amount greater than the minimum
     */
    List<VentaDto> obtenerVentasPorMontoTotalMayorQue(BigDecimal montoMinimo);

    /**
     * Get sales by payment method.
     *
     * @param metodoPago the payment method to search for
     * @return the list of sales with the payment method
     */
    List<VentaDto> obtenerVentasPorMetodoPago(String metodoPago);
}
