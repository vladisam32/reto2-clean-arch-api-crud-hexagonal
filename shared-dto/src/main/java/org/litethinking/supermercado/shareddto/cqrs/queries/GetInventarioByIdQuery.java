package org.litethinking.supermercado.shareddto.cqrs.queries;

/**
 * Query for getting an inventory record by its id.
 * This is an immutable record class that represents the query to get an inventory by id.
 */
public record GetInventarioByIdQuery(
    Long id
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}