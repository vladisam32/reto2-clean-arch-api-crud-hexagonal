package org.litethinking.supermercado.shareddto.supermercado;

/**
 * DTO for Cliente entity.
 * This is an immutable record class that represents a customer in the system.
 */
public record ClienteDto(
    Long id,
    String nombre,
    String email,
    String telefono,
    String direccion
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}