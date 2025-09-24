package org.litethinking.supermercado.shareddto.supermercado.inventario;

import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;

import java.time.LocalDate;

/**
 * DTO for Inventario entity.
 * This is an immutable record class that represents inventory in the system.
 */
public record InventarioDto(
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