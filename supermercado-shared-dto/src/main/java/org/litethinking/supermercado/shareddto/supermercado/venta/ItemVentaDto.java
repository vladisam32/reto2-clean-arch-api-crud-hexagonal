package org.litethinking.supermercado.shareddto.supermercado.venta;

import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;

import java.math.BigDecimal;

/**
 * DTO for ItemVenta entity.
 * This is an immutable record class that represents a sale item in the system.
 */
public record ItemVentaDto(
    Long id,
    ProductoDto producto,
    Integer cantidad,
    BigDecimal precioUnitario,
    BigDecimal subtotal
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}