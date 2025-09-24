package org.litethinking.supermercado.infrastructure.entity.inventario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.litethinking.supermercado.infrastructure.entity.EntidadJpaProducto;

import java.time.LocalDate;

/**
 * JPA entity for Inventario.
 */
@Entity
@Table(name = "inventarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntidadJpaInventario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private EntidadJpaProducto producto;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    private Integer stockMinimo;
    
    private Integer stockMaximo;
    
    private LocalDate fechaUltimaReposicion;
    
    private String ubicacion;
}