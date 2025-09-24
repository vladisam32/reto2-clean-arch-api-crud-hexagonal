package org.litethinking.supermercado.shareddto.cqrs.commands;

/**
 * Command for updating the quantity of an existing inventory record.
 * This is an immutable record class that represents the command to update the quantity of an inventory.
 */
public record UpdateInventarioCantidadCommand(
    Long id,
    Integer cantidad
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}