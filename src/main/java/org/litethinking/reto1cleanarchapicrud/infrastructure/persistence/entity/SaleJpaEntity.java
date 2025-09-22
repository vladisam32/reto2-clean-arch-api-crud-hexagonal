package org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.entity;

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
 * JPA entity pa' la venta, esto guarda to' lo de la venta en la base de datos
 * Aquí guardamo' la fecha, el cliente y el total, ya tu sabe!
 */
@Entity
@Table(name = "sales")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime saleDate;

    @Column(length = 100)
    private String customerName;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column
    private String paymentMethod;

    @Column
    private String status;

    @Column(length = 100)
    private String cashierName;

    @Column(unique = true)
    private String receiptNumber;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItemJpaEntity> items = new ArrayList<>();

    /**
     * Método pa' añadir un item a la venta, bien fácil
     * @param item el item que vamo' a añadir
     */
    public void addItem(SaleItemJpaEntity item) {
        items.add(item);
        item.setSale(this);
    }

    /**
     * Método pa' quitar un item de la venta si el cliente se arrepiente
     * @param item el item que vamo' a quitar
     */
    public void removeItem(SaleItemJpaEntity item) {
        items.remove(item);
        item.setSale(null);
    }
}
