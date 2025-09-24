package org.litethinking.supermercado.domain.model.venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Venta entity representing a transaction in the system.
 */
public class Venta {
    private Long id;
    private LocalDateTime fechaVenta;
    private String nombreCliente;
    private List<ItemVenta> items;
    private BigDecimal montoTotal;
    private String metodoPago;

    // Default constructor
    public Venta() {
    }

    // All-args constructor
    public Venta(Long id, LocalDateTime fechaVenta, String nombreCliente, List<ItemVenta> items, BigDecimal montoTotal, String metodoPago) {
        this.id = id;
        this.fechaVenta = fechaVenta;
        this.nombreCliente = nombreCliente;
        this.items = items;
        this.montoTotal = montoTotal;
        this.metodoPago = metodoPago;
    }

    // Manual implementation of builder pattern
    public static VentaBuilder builder() {
        return new VentaBuilder();
    }

    public static class VentaBuilder {
        private Long id;
        private LocalDateTime fechaVenta;
        private String nombreCliente;
        private List<ItemVenta> items;
        private BigDecimal montoTotal;
        private String metodoPago;

        VentaBuilder() {
        }

        public VentaBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public VentaBuilder fechaVenta(LocalDateTime fechaVenta) {
            this.fechaVenta = fechaVenta;
            return this;
        }

        public VentaBuilder nombreCliente(String nombreCliente) {
            this.nombreCliente = nombreCliente;
            return this;
        }

        public VentaBuilder items(List<ItemVenta> items) {
            this.items = items;
            return this;
        }

        public VentaBuilder montoTotal(BigDecimal montoTotal) {
            this.montoTotal = montoTotal;
            return this;
        }

        public VentaBuilder metodoPago(String metodoPago) {
            this.metodoPago = metodoPago;
            return this;
        }

        public Venta build() {
            return new Venta(id, fechaVenta, nombreCliente, items, montoTotal, metodoPago);
        }
    }

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
