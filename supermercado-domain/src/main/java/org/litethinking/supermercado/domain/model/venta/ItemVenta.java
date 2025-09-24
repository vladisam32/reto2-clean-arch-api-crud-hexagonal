package org.litethinking.supermercado.domain.model.venta;

import org.litethinking.supermercado.domain.model.Producto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * ItemVenta entity representing an item in a sale.
 */
public class ItemVenta {
    private Long id;
    private Producto producto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    // Default constructor
    public ItemVenta() {
    }

    // All-args constructor
    public ItemVenta(Long id, Producto producto, Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    // Manual implementation of builder pattern
    public static ItemVentaBuilder builder() {
        return new ItemVentaBuilder();
    }

    public static class ItemVentaBuilder {
        private Long id;
        private Producto producto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;

        ItemVentaBuilder() {
        }

        public ItemVentaBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ItemVentaBuilder producto(Producto producto) {
            this.producto = producto;
            return this;
        }

        public ItemVentaBuilder cantidad(Integer cantidad) {
            this.cantidad = cantidad;
            return this;
        }

        public ItemVentaBuilder precioUnitario(BigDecimal precioUnitario) {
            this.precioUnitario = precioUnitario;
            return this;
        }

        public ItemVentaBuilder subtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
            return this;
        }

        public ItemVenta build() {
            return new ItemVenta(id, producto, cantidad, precioUnitario, subtotal);
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Producto getProducto() {
        return producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    // equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemVenta itemVenta = (ItemVenta) o;
        return Objects.equals(id, itemVenta.id) &&
               Objects.equals(producto, itemVenta.producto) &&
               Objects.equals(cantidad, itemVenta.cantidad) &&
               Objects.equals(precioUnitario, itemVenta.precioUnitario) &&
               Objects.equals(subtotal, itemVenta.subtotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, producto, cantidad, precioUnitario, subtotal);
    }

    @Override
    public String toString() {
        return "ItemVenta(id=" + id + ", producto=" + producto + ", cantidad=" + cantidad + 
               ", precioUnitario=" + precioUnitario + ", subtotal=" + subtotal + ")";
    }
}
