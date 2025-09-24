package org.litethinking.supermercado.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA entity for Cajero.
 */
@Entity
@Table(name = "cajeros")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntidadJpaCajero {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(unique = true, nullable = false)
    private String codigo;
    
    @Column(nullable = false)
    private String turno;
}