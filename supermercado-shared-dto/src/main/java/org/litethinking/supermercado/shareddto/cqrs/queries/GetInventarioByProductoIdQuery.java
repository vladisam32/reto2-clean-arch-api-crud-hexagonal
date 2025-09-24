package org.litethinking.supermercado.shareddto.cqrs.queries;

/**
 * Query for getting an inventory record by its product id.
 * This is an immutable record class that represents the query to get an inventory by product id.
 */
public record GetInventarioByProductoIdQuery(
    Long productoId
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}