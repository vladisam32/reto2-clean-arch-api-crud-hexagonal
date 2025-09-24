package org.litethinking.supermercado.shareddto.cqrs.commands;

/**
 * Command DTO for creating a new cashier.
 * This is an immutable record class that represents a command to create a cashier.
 */
public record CreateCajeroCommand(
    String nombre,
    String codigo,
    String turno
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}