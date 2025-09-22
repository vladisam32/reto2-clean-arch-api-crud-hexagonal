package org.litethinking.supermercado.domain.model.venta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Venta entity representing a transaction in the system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Venta {
    private Long id;
    private LocalDateTime fechaVenta;
    private String nombreCliente;
    private List<ItemVenta> items;
    private BigDecimal montoTotal;
    private String metodoPago;


    // Getters
    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public List<ItemVenta> getItems() {
        return items;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setItems(List<ItemVenta> items) {
        this.items = items;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    // equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venta venta = (Venta) o;
        return Objects.equals(id, venta.id) &&
               Objects.equals(fechaVenta, venta.fechaVenta) &&
               Objects.equals(nombreCliente, venta.nombreCliente) &&
               Objects.equals(items, venta.items) &&
               Objects.equals(montoTotal, venta.montoTotal) &&
               Objects.equals(metodoPago, venta.metodoPago);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaVenta, nombreCliente, items, montoTotal, metodoPago);
    }

    @Override
    public String toString() {
        return "Venta(id=" + id + ", fechaVenta=" + fechaVenta + ", nombreCliente=" + nombreCliente + 
               ", items=" + items + ", montoTotal=" + montoTotal + ", metodoPago=" + metodoPago + ")";
    }
}
