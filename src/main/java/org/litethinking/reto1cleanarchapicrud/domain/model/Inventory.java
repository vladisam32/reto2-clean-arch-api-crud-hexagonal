package org.litethinking.reto1cleanarchapicrud.domain.model.inventario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.litethinking.reto1cleanarchapicrud.domain.model.supermercado.Producto;

import java.time.LocalDateTime;

/**
 * Inventario entity representing the stock of a product in the supermarket.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {
    private Long id;
    private Producto producto;
    private Integer cantidad;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private LocalDateTime fechaUltimaReposicion;
    private String ubicacion;
}
