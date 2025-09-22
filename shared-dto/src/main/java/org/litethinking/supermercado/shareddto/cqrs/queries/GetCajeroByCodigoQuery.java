package org.litethinking.supermercado.shareddto.cqrs.queries;

/**
 * Query DTO for retrieving a cashier by its code.
 * This is an immutable record class that represents a query to get a cashier by code.
 */
public record GetCajeroByCodigoQuery(
    String codigo
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}