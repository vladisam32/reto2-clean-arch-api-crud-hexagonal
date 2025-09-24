package org.litethinking.supermercado.shareddto.cqrs.commands;

import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;

import java.time.LocalDate;

/**
 * Command for updating an existing inventory record.
 * This is an immutable record class that represents the command to update an inventory.
 */
public record UpdateInventarioCommand(
    Long id,
    ProductoDto producto,
    Integer cantidad,
    Integer stockMinimo,
    Integer stockMaximo,
    LocalDate fechaUltimaReposicion,
    String ubicacion
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}