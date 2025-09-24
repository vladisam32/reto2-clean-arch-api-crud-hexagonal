package org.litethinking.supermercado.shareddto.supermercado.venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Venta entity.
 * This is an immutable record class that represents a sale in the system.
 */
public record VentaDto(
    Long id,
    LocalDateTime fechaVenta,
    String nombreCliente,
    List<ItemVentaDto> items,
    BigDecimal montoTotal,
    String metodoPago
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}