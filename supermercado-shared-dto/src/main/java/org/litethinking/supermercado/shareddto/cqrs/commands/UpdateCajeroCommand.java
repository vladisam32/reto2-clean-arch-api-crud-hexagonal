package org.litethinking.supermercado.shareddto.cqrs.commands;

/**
 * Command DTO for updating an existing cashier.
 * This is an immutable record class that represents a command to update a cashier.
 */
public record UpdateCajeroCommand(
    Long id,
    String nombre,
    String codigo,
    String turno
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}