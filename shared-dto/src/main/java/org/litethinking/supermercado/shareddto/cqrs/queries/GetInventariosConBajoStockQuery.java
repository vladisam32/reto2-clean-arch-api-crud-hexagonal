package org.litethinking.supermercado.shareddto.cqrs.queries;

/**
 * Query for getting inventory records with low stock.
 * This is an immutable record class that represents the query to get inventories with low stock.
 */
public record GetInventariosConBajoStockQuery() {
    // Records automatically provide getters, equals, hashCode, and toString methods
}