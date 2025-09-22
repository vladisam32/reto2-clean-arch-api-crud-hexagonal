package org.litethinking.supermercado.infrastructure.entity.venta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.litethinking.supermercado.infrastructure.entity.EntidadJpaProducto;

import java.math.BigDecimal;

/**
 * JPA entity for ItemVenta.
 */
@Entity
@Table(name = "items_venta")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntidadJpaItemVenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private EntidadJpaProducto producto;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(nullable = false)
    private BigDecimal precioUnitario;
    
    @Column(nullable = false)
    private BigDecimal subtotal;
    
    @ManyToOne
    @JoinColumn(name = "venta_id")
    private EntidadJpaVenta venta;
}