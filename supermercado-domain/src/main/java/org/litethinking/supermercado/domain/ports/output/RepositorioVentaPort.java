package org.litethinking.supermercado.domain.ports.output;

import org.litethinking.supermercado.domain.model.venta.Venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto secundario (salida) para operaciones de persistencia de Venta.
 * Define las operaciones que cualquier adaptador de persistencia debe implementar.
 */
public interface RepositorioVentaPort {
    
    /**
     * Guarda una venta.
     *
     * @param venta la venta a guardar
     * @return la venta guardada
     */
    Venta save(Venta venta);
    
    /**
     * Busca una venta por su id.
     *
     * @param id el id de la venta
     * @return la venta si se encuentra, vacío en caso contrario
     */
    Optional<Venta> findById(Long id);
    
    /**
     * Busca todas las ventas.
     *
     * @return la lista de ventas
     */
    List<Venta> findAll();
    
    /**
     * Elimina una venta por su id.
     *
     * @param id el id de la venta a eliminar
     */
    void deleteById(Long id);
    
    /**
     * Busca ventas por rango de fecha.
     *
     * @param fechaInicio la fecha de inicio
     * @param fechaFin la fecha de fin
     * @return la lista de ventas en el rango de fecha
     */
    List<Venta> findByFechaVentaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Busca ventas por nombre de cliente.
     *
     * @param nombreCliente el nombre del cliente a buscar
     * @return la lista de ventas del cliente
     */
    List<Venta> findByNombreCliente(String nombreCliente);
    
    /**
     * Busca ventas por monto total mayor que.
     *
     * @param montoMinimo el monto mínimo
     * @return la lista de ventas con monto total mayor que el mínimo
     */
    List<Venta> findByMontoTotalGreaterThan(BigDecimal montoMinimo);
    
    /**
     * Busca ventas por método de pago.
     *
     * @param metodoPago el método de pago a buscar
     * @return la lista de ventas con el método de pago
     */
    List<Venta> findByMetodoPago(String metodoPago);
}