package org.litethinking.supermercado.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitario para la entidad Producto.
 * Verifica el comportamiento de los métodos de la entidad, incluyendo validación y lógica de negocio.
 */
public class ProductoTest {

    @Test
    @DisplayName("Debería crear un producto válido usando el builder")
    public void testCrearProductoValido() {
        // Arrange
        Long id = 1L;
        String nombre = "Producto Test";
        String descripcion = "Descripción del producto test";
        BigDecimal precio = new BigDecimal("10.99");
        String categoria = "Electrónica";
        String codigoBarras = "1234567890";

        // Act
        Producto producto = Producto.builder()
                .id(id)
                .nombre(nombre)
                .descripcion(descripcion)
                .precio(precio)
                .categoria(categoria)
                .codigoBarras(codigoBarras)
                .build();

        // Assert
        assertEquals(id, producto.getId());
        assertEquals(nombre, producto.getNombre());
        assertEquals(descripcion, producto.getDescripcion());
        assertEquals(precio, producto.getPrecio());
        assertEquals(categoria, producto.getCategoria());
        assertEquals(codigoBarras, producto.getCodigoBarras());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el nombre es nulo")
    public void testNombreNulo() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Producto.builder()
                    .id(1L)
                    .nombre(null)
                    .descripcion("Descripción")
                    .precio(new BigDecimal("10.99"))
                    .categoria("Electrónica")
                    .codigoBarras("1234567890")
                    .build();
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el nombre está vacío")
    public void testNombreVacio() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Producto.builder()
                    .id(1L)
                    .nombre("")
                    .descripcion("Descripción")
                    .precio(new BigDecimal("10.99"))
                    .categoria("Electrónica")
                    .codigoBarras("1234567890")
                    .build();
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el precio es nulo")
    public void testPrecioNulo() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Producto.builder()
                    .id(1L)
                    .nombre("Producto Test")
                    .descripcion("Descripción")
                    .precio(null)
                    .categoria("Electrónica")
                    .codigoBarras("1234567890")
                    .build();
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el precio es negativo")
    public void testPrecioNegativo() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Producto.builder()
                    .id(1L)
                    .nombre("Producto Test")
                    .descripcion("Descripción")
                    .precio(new BigDecimal("-10.99"))
                    .categoria("Electrónica")
                    .codigoBarras("1234567890")
                    .build();
        });
    }

    @Test
    @DisplayName("Debería calcular correctamente el precio con impuesto")
    public void testCalcularPrecioConImpuesto() {
        // Arrange
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("100.00"))
                .categoria("Electrónica")
                .codigoBarras("1234567890")
                .build();
        BigDecimal tasaImpuesto = new BigDecimal("0.16"); // 16% de impuesto

        // Act
        BigDecimal precioConImpuesto = producto.calcularPrecioConImpuesto(tasaImpuesto);

        // Assert
        assertEquals(new BigDecimal("116.00"), precioConImpuesto);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando la tasa de impuesto es negativa")
    public void testTasaImpuestoNegativa() {
        // Arrange
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("100.00"))
                .categoria("Electrónica")
                .codigoBarras("1234567890")
                .build();
        BigDecimal tasaImpuestoNegativa = new BigDecimal("-0.16");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            producto.calcularPrecioConImpuesto(tasaImpuestoNegativa);
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando la tasa de impuesto es nula")
    public void testTasaImpuestoNula() {
        // Arrange
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("100.00"))
                .categoria("Electrónica")
                .codigoBarras("1234567890")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            producto.calcularPrecioConImpuesto(null);
        });
    }

    @Test
    @DisplayName("Debería verificar la igualdad de dos productos")
    public void testEquals() {
        // Arrange
        Producto producto1 = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("100.00"))
                .categoria("Electrónica")
                .codigoBarras("1234567890")
                .build();

        Producto producto2 = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("100.00"))
                .categoria("Electrónica")
                .codigoBarras("1234567890")
                .build();

        Producto productoDistinto = Producto.builder()
                .id(2L)
                .nombre("Otro Producto")
                .descripcion("Otra descripción")
                .precio(new BigDecimal("200.00"))
                .categoria("Hogar")
                .codigoBarras("0987654321")
                .build();

        // Assert
        assertEquals(producto1, producto2);
        assertNotEquals(producto1, productoDistinto);
        assertNotEquals(producto1, null);
        assertNotEquals(producto1, new Object());
    }

    @Test
    @DisplayName("Debería generar el mismo hashCode para productos iguales")
    public void testHashCode() {
        // Arrange
        Producto producto1 = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("100.00"))
                .categoria("Electrónica")
                .codigoBarras("1234567890")
                .build();

        Producto producto2 = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("100.00"))
                .categoria("Electrónica")
                .codigoBarras("1234567890")
                .build();

        // Assert
        assertEquals(producto1.hashCode(), producto2.hashCode());
    }

    @Test
    @DisplayName("Debería generar una representación en String correcta")
    public void testToString() {
        // Arrange
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("100.00"))
                .categoria("Electrónica")
                .codigoBarras("1234567890")
                .build();

        // Act
        String toString = producto.toString();

        // Assert
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nombre=Producto Test"));
        assertTrue(toString.contains("descripcion=Descripción"));
        assertTrue(toString.contains("precio=100.00"));
        assertTrue(toString.contains("categoria=Electrónica"));
        assertTrue(toString.contains("codigoBarras=1234567890"));
    }
}