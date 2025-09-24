package org.litethinking.supermercado.shareddto.cqrs.queries;

/**
 * Query DTO for retrieving all customers.
 * This is an immutable record class that represents a query to get all customers.
 */
public record GetAllClientesQuery() {
    // Records automatically provide getters, equals, hashCode, and toString methods
}