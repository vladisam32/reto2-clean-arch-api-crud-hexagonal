package org.litethinking.reto1cleanarchapicrud.domain.model.venta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.litethinking.reto1cleanarchapicrud.domain.model.supermercado.Producto;

import java.math.BigDecimal;

/**
 * ItemVenta entity representing an individual item in a sale transaction.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemVenta {
    private Long id;
    private Producto producto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private BigDecimal descuento;
}
