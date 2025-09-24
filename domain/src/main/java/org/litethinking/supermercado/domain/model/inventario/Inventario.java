package org.litethinking.supermercado.domain.model.inventario;

import org.litethinking.supermercado.domain.model.Producto;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Inventario entity representing the stock of a product.
 */
public class Inventario {
    private Long id;
    private Producto producto;
    private Integer cantidad;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private LocalDate fechaUltimaReposicion;
    private String ubicacion;

    // Default constructor
    public Inventario() {
    }

    // All-args constructor
    public Inventario(Long id, Producto producto, Integer cantidad, Integer stockMinimo, Integer stockMaximo, LocalDate fechaUltimaReposicion, String ubicacion) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
        this.fechaUltimaReposicion = fechaUltimaReposicion;
        this.ubicacion = ubicacion;
    }

    // Manual implementation of builder pattern
    public static InventarioBuilder builder() {
        return new InventarioBuilder();
    }

    public static class InventarioBuilder {
        private Long id;
        private Producto producto;
        private Integer cantidad;
        private Integer stockMinimo;
        private Integer stockMaximo;
        private LocalDate fechaUltimaReposicion;
        private String ubicacion;

        InventarioBuilder() {
        }

        public InventarioBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public InventarioBuilder producto(Producto producto) {
            this.producto = producto;
            return this;
        }

        public InventarioBuilder cantidad(Integer cantidad) {
            this.cantidad = cantidad;
            return this;
        }

        public InventarioBuilder stockMinimo(Integer stockMinimo) {
            this.stockMinimo = stockMinimo;
            return this;
        }

        public InventarioBuilder stockMaximo(Integer stockMaximo) {
            this.stockMaximo = stockMaximo;
            return this;
        }

        public InventarioBuilder fechaUltimaReposicion(LocalDate fechaUltimaReposicion) {
            this.fechaUltimaReposicion = fechaUltimaReposicion;
            return this;
        }

        public InventarioBuilder ubicacion(String ubicacion) {
            this.ubicacion = ubicacion;
            return this;
        }

        public Inventario build() {
            return new Inventario(id, producto, cantidad, stockMinimo, stockMaximo, fechaUltimaReposicion, ubicacion);
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

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public Integer getStockMaximo() {
        return stockMaximo;
    }

    public LocalDate getFechaUltimaReposicion() {
        return fechaUltimaReposicion;
    }

    public String getUbicacion() {
        return ubicacion;
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

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void setStockMaximo(Integer stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public void setFechaUltimaReposicion(LocalDate fechaUltimaReposicion) {
        this.fechaUltimaReposicion = fechaUltimaReposicion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    // equals, hashCode, and toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventario inventario = (Inventario) o;
        return Objects.equals(id, inventario.id) &&
               Objects.equals(producto, inventario.producto) &&
               Objects.equals(cantidad, inventario.cantidad) &&
               Objects.equals(stockMinimo, inventario.stockMinimo) &&
               Objects.equals(stockMaximo, inventario.stockMaximo) &&
               Objects.equals(fechaUltimaReposicion, inventario.fechaUltimaReposicion) &&
               Objects.equals(ubicacion, inventario.ubicacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, producto, cantidad, stockMinimo, stockMaximo, fechaUltimaReposicion, ubicacion);
    }

    @Override
    public String toString() {
        return "Inventario(id=" + id + ", producto=" + producto + ", cantidad=" + cantidad + 
               ", stockMinimo=" + stockMinimo + ", stockMaximo=" + stockMaximo + 
               ", fechaUltimaReposicion=" + fechaUltimaReposicion + ", ubicacion=" + ubicacion + ")";
    }
}
