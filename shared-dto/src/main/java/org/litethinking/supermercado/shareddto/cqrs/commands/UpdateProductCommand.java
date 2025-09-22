package org.litethinking.supermercado.shareddto.cqrs.commands;

import java.math.BigDecimal;

/**
 * Command DTO for updating an existing product.
 * This is an immutable record class that represents a command to update a product.
 */
public record UpdateProductCommand(
    Long id,
    String nombre,
    String descripcion,
    BigDecimal precio,
    String categoria,
    String codigoBarras
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}