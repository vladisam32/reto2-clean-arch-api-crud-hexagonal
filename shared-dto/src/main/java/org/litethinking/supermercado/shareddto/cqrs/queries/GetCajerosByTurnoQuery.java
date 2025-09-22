package org.litethinking.supermercado.shareddto.cqrs.queries;

/**
 * Query DTO for retrieving cashiers by their shift.
 * This is an immutable record class that represents a query to get cashiers by shift.
 */
public record GetCajerosByTurnoQuery(
    String turno
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}