package org.litethinking.supermercado.shareddto.cqrs.commands;

/**
 * Command DTO for deleting a cashier.
 * This is an immutable record class that represents a command to delete a cashier.
 */
public record DeleteCajeroCommand(
    Long id
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}