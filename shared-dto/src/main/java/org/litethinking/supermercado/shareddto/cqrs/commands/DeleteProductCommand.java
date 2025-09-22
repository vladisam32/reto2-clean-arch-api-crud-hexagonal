package org.litethinking.supermercado.shareddto.cqrs.commands;

/**
 * Command DTO for deleting an existing product.
 * This is an immutable record class that represents a command to delete a product.
 */
public record DeleteProductCommand(
    Long id
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}