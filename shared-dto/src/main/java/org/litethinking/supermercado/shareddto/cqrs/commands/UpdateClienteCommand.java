package org.litethinking.supermercado.shareddto.cqrs.commands;

/**
 * Command DTO for updating an existing customer.
 * This is an immutable record class that represents a command to update a customer.
 */
public record UpdateClienteCommand(
    Long id,
    String nombre,
    String email,
    String telefono,
    String direccion
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}