package org.litethinking.supermercado.shareddto.cqrs.commands;

/**
 * Command for deleting an existing inventory record.
 * This is an immutable record class that represents the command to delete an inventory.
 */
public record DeleteInventarioCommand(
    Long id
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}