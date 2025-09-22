package org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * JPA entity pa' los items de venta, aquí guardamo' lo que la gente compra
 * Con esto sabemo' qué producto, cuánto y a qué precio se vendió, tú sabe!
 */
@Entity
@Table(name = "sale_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductJpaEntity product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private BigDecimal subtotal;

    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private SaleJpaEntity sale;
}
