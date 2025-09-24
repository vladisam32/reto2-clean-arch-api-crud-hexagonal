package org.litethinking.supermercado.shareddto.cqrs.queries;

/**
 * Query DTO for retrieving a customer by its ID.
 * This is an immutable record class that represents a query to get a customer.
 */
public record GetClienteByIdQuery(
    Long id
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}