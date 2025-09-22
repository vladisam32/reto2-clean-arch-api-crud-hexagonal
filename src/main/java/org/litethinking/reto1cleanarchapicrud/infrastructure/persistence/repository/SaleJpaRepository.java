package org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.repository;

import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.entity.SaleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA pa' las ventas, aquí guardamo' to' lo que se vende
 * Con esto podemo' ver las ventas del día, la semana o el mes, tú sabe!
 */
@Repository
public interface SaleJpaRepository extends JpaRepository<SaleJpaEntity, Long> {

    /**
     * Busca ventas por nombre del cliente, pa' cuando queremo' ver qué compró
     * @param customerName el nombre del cliente que tamo' buscando
     * @return la lista de ventas de ese cliente
     */
    List<SaleJpaEntity> findByCustomerNameContainingIgnoreCase(String customerName);

    /**
     * Busca ventas por fecha, pa' ver cómo nos fue en un día
     * @param startDate desde cuándo queremo' ver
     * @param endDate hasta cuándo queremo' ver
     * @return la lista de ventas en ese período
     */
    List<SaleJpaEntity> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca ventas por método de pago, pa' ver cuánto se vendió con tarjeta o efectivo
     * @param paymentMethod el método de pago que tamo' buscando
     * @return la lista de ventas con ese método de pago
     */
    List<SaleJpaEntity> findByPaymentMethod(String paymentMethod);

    /**
     * Busca ventas por monto total, pa' ver las ventas grandes
     * @param amount el monto mínimo que tamo' buscando
     * @return la lista de ventas grandes
     */
    List<SaleJpaEntity> findByTotalAmountGreaterThanEqual(BigDecimal amount);

    /**
     * Calcula el total de ventas por día, pa' ver cómo va el negocio
     * @param date el día que queremo' ver
     * @return el total de ventas de ese día
     */
    @Query("SELECT SUM(s.totalAmount) FROM SaleJpaEntity s WHERE DATE(s.saleDate) = DATE(:date)")
    BigDecimal getTotalSalesByDate(@Param("date") LocalDateTime date);

    /**
     * Busca ventas por cajero, pa' ver quién vende más
     * @param cashierName el nombre del cajero que tamo' buscando
     * @return la lista de ventas de ese cajero
     */
    List<SaleJpaEntity> findByCashierNameContainingIgnoreCase(String cashierName);

    /**
     * Busca una venta por número de recibo, pa' cuando hay una queja
     * @param receiptNumber el número de recibo que tamo' buscando
     * @return la venta si la encontramo', si no, na'
     */
    Optional<SaleJpaEntity> findByReceiptNumber(String receiptNumber);
}
