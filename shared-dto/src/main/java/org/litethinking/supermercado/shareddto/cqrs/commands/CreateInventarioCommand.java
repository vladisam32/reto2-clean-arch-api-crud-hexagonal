package org.litethinking.supermercado.shareddto.cqrs.commands;

import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;

import java.time.LocalDate;

/**
 * Command for creating a new inventory record.
 * This is an immutable record class that represents the command to create a new inventory.
 */
public record CreateInventarioCommand(
    ProductoDto producto,
    Integer cantidad,
    Integer stockMinimo,
    Integer stockMaximo,
    LocalDate fechaUltimaReposicion,
    String ubicacion
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}