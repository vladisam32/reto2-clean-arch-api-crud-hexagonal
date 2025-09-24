package org.litethinking.supermercado.shareddto.cqrs.queries;

/**
 * Query for getting inventory records by their location.
 * This is an immutable record class that represents the query to get inventories by location.
 */
public record GetInventariosByUbicacionQuery(
    String ubicacion
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}