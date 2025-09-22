package org.litethinking.supermercado.shareddto.cqrs.queries;

/**
 * Query DTO for retrieving a product by its ID.
 * This is an immutable record class that represents a query to get a product.
 */
public record GetProductByIdQuery(
    Long id
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}