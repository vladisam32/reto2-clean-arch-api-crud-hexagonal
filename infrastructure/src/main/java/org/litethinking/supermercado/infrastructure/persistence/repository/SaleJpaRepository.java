package org.litethinking.supermercado.infrastructure.persistence.repository;

import org.litethinking.supermercado.infrastructure.entity.venta.EntidadJpaVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA repository for EntidadJpaVenta.
 */
@Repository
public interface SaleJpaRepository extends JpaRepository<EntidadJpaVenta, Long> {

    /**
     * Find sales by customer name containing the given string (case insensitive).
     * 
     * @param nombreCliente the customer name to search for
     * @return the list of sales matching the customer name
     */
    List<EntidadJpaVenta> findByNombreClienteContainingIgnoreCase(String nombreCliente);

    /**
     * Find sales between the given dates.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return the list of sales between the dates
     */
    List<EntidadJpaVenta> findByFechaVentaBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find sales by customer name.
     * 
     * @param nombreCliente the customer name to search for
     * @return the list of sales for the customer
     */
    List<EntidadJpaVenta> findByNombreCliente(String nombreCliente);
}
