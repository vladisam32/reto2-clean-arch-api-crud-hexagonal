package org.litethinking.supermercado.shareddto.supermercado;

import java.math.BigDecimal;

/**
 * DTO for Producto entity.
 * This is an immutable record class that represents a product in the system.
 */
public record ProductoDto(
    Long id,
    String nombre,
    String descripcion,
    BigDecimal precio,
    String categoria,
    String codigoBarras
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}