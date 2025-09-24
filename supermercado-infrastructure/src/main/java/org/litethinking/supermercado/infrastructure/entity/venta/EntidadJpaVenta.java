package org.litethinking.supermercado.infrastructure.entity.venta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity for Venta.
 */
@Entity
@Table(name = "ventas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntidadJpaVenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime fechaVenta;
    
    private String nombreCliente;
    
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntidadJpaItemVenta> items = new ArrayList<>();
    
    @Column(nullable = false)
    private BigDecimal montoTotal;
    
    private String metodoPago;
    
    public void addItem(EntidadJpaItemVenta item) {
        items.add(item);
        item.setVenta(this);
    }
    
    public void removeItem(EntidadJpaItemVenta item) {
        items.remove(item);
        item.setVenta(null);
    }
}