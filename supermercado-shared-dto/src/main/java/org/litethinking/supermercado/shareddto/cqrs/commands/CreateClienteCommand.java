package org.litethinking.supermercado.shareddto.cqrs.commands;

/**
 * Command DTO for creating a new customer.
 * This is an immutable record class that represents a command to create a customer.
 */
public record CreateClienteCommand(
    String nombre,
    String email,
    String telefono,
    String direccion
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}