package org.litethinking.supermercado.shareddto.cqrs.queries;

import java.math.BigDecimal;

/**
 * Query DTO for retrieving products by various filters.
 * This is an immutable record class that represents a query to get products by filter.
 * All fields are optional and can be null, indicating that the filter should not be applied.
 */
public record GetProductsByFilterQuery(
    String categoria,
    BigDecimal precioMinimo,
    BigDecimal precioMaximo,
    String codigoBarras
) {
    // Records automatically provide getters, equals, hashCode, and toString methods
}