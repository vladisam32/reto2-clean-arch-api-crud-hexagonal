package org.litethinking.supermercado.shareddto.cqrs.queries;

/**
 * Query DTO for retrieving a customer by its email.
 * This is an immutable record class that represents a query to get a customer by email.
 */
public record GetClienteByEmailQuery(
    String email
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}