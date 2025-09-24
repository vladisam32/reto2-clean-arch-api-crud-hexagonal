package org.litethinking.supermercado.shareddto.supermercado;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

/**
 * DTO for Cajero entity.
 * This is an immutable record class that represents a cashier in the system.
 */

@Builder
public record CajeroDto(
    Long id,
    String nombre,
    String codigo,
    String turno
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}