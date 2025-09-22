package org.litethinking.supermercado.shareddto.cqrs.queries;

/**
 * Query DTO for retrieving customers by name containing a substring.
 * This is an immutable record class that represents a query to get customers by name.
 */
public record GetClientesByNombreContainingQuery(
    String nombre
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}