package org.litethinking.supermercado.infrastructure.persistence.repository.venta;

import org.litethinking.supermercado.infrastructure.entity.venta.EntidadJpaVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA repository for EntidadJpaVenta.
 */
@Repository
public interface RepositorioJpaVenta extends JpaRepository<EntidadJpaVenta, Long> {

    /**
     * Find sales by date range.
     *
     * @param fechaInicio the start date
     * @param fechaFin the end date
     * @return the list of sales in the date range
     */
    List<EntidadJpaVenta> findByFechaVentaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Find sales by customer name.
     *
     * @param nombreCliente the customer name to search for
     * @return the list of sales for the customer
     */
    List<EntidadJpaVenta> findByNombreCliente(String nombreCliente);

    /**
     * Find sales by total amount greater than.
     *
     * @param montoMinimo the minimum total amount
     * @return the list of sales with total amount greater than the minimum
     */
    List<EntidadJpaVenta> findByMontoTotalGreaterThan(BigDecimal montoMinimo);

    /**
     * Find sales by payment method.
     *
     * @param metodoPago the payment method to search for
     * @return the list of sales with the payment method
     */
    List<EntidadJpaVenta> findByMetodoPago(String metodoPago);
}
