package org.litethinking.supermercado.shareddto.cqrs.commands;

/**
 * Command DTO for deleting a customer.
 * This is an immutable record class that represents a command to delete a customer.
 */
public record DeleteClienteCommand(
    Long id
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}