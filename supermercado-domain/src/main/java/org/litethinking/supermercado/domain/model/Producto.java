package org.litethinking.supermercado.domain.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidad Producto que representa un producto en el sistema de inventario.
 * Implementa el patrón de diseño de Entidad con encapsulamiento y validación.
 */

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Producto {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String categoria;
    private String codigoBarras;

   /* // Constructor privado para forzar el uso del builder
    private Producto(Long id, String nombre, String descripcion, BigDecimal precio, String categoria, String codigoBarras) {
        this.id = id;
        this.setNombre(nombre);
        this.setDescripcion(descripcion);
        this.setPrecio(precio);
        this.setCategoria(categoria);
        this.setCodigoBarras(codigoBarras);
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String nombre;
        private String descripcion;
        private BigDecimal precio;
        private String categoria;
        private String codigoBarras;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public Builder precio(BigDecimal precio) {
            this.precio = precio;
            return this;
        }

        public Builder categoria(String categoria) {
            this.categoria = categoria;
            return this;
        }

        public Builder codigoBarras(String codigoBarras) {
            this.codigoBarras = codigoBarras;
            return this;
        }

        public Producto build() {
            return new Producto(id, nombre, descripcion, precio, categoria, codigoBarras);
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    // Setters con validación
    public void setId(Long id) {
        this.id = id;
    }*/

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            log.error("Valida nombre del producto no puede estar vacio");
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        this.nombre = nombre;
    }

   /* public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(BigDecimal precio) {
        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio del producto no puede ser negativo");
        }
        this.precio = precio;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    // Métodos de negocio
    public boolean tieneStock(int cantidad) {
        // Aquí se implementaría la lógica para verificar si hay stock suficiente
        // Por ahora, es un método de ejemplo
        return true;
    }*/

    public BigDecimal calcularPrecioConImpuesto(BigDecimal tasaImpuesto) {
        if (tasaImpuesto == null || tasaImpuesto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La tasa de impuesto no puede ser negativa");
        }
        return precio.add(precio.multiply(tasaImpuesto)).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    // equals, hashCode, y toString
  /*  @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id) &&
               Objects.equals(nombre, producto.nombre) &&
               Objects.equals(descripcion, producto.descripcion) &&
               Objects.equals(precio, producto.precio) &&
               Objects.equals(categoria, producto.categoria) &&
               Objects.equals(codigoBarras, producto.codigoBarras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, descripcion, precio, categoria, codigoBarras);
    }

    @Override
    public String toString() {
        return "Producto(id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + 
               ", precio=" + precio + ", categoria=" + categoria + ", codigoBarras=" + codigoBarras + ")";
    }*/
}
