package org.litethinking.supermercado.shareddto.cqrs.commands;

import java.math.BigDecimal;

/**
 * Command DTO for creating a new product.
 * This is an immutable record class that represents a command to create a product.
 */
public record CreateProductCommand(
    String nombre,
    String descripcion,
    BigDecimal precio,
    String categoria,
    String codigoBarras
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}