package org.litethinking.supermercado.shareddto.cqrs.queries;

/**
 * Query DTO for retrieving all products.
 * This is an immutable record class that represents a query to get all products.
 */
public record GetAllProductsQuery() {
    // Records automatically provide getters, equals, hashCode, and toString methods
}