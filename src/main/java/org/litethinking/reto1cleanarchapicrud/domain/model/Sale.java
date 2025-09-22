package org.litethinking.reto1cleanarchapicrud.domain.model.venta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Venta entity representing a sale transaction in the supermarket.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Venta {
    private Long id;
    private LocalDateTime fechaVenta;
    private List<ItemVenta> items;
    private BigDecimal montoTotal;
    private String metodoPago;
    private String nombreCliente;
    private String nombreCajero;
    private String numeroRecibo;
}
