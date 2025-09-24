package org.litethinking.supermercado.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitario para la entidad Cajero.
 * Verifica el comportamiento de los métodos de la entidad.
 */
public class CajeroTest {

    @Test
    @DisplayName("Debería crear un cajero válido usando el builder")
    public void testCrearCajeroValido() {
        // Arrange
        Long id = 1L;
        String nombre = "Carlos Rodríguez";
        String codigo = "CAJ001";
        String turno = "Mañana";

        // Act
        Cajero cajero = Cajero.builder()
                .id(id)
                .nombre(nombre)
                .codigo(codigo)
                .turno(turno)
                .build();

        // Assert
        assertEquals(id, cajero.getId());
        assertEquals(nombre, cajero.getNombre());
        assertEquals(codigo, cajero.getCodigo());
        assertEquals(turno, cajero.getTurno());
    }

    @Test
    @DisplayName("Debería crear un cajero con valores nulos")
    public void testCrearCajeroConValoresNulos() {
        // Act
        Cajero cajero = Cajero.builder()
                .id(1L)
                .nombre(null)
                .codigo(null)
                .turno(null)
                .build();

        // Assert
        assertNull(cajero.getNombre());
        assertNull(cajero.getCodigo());
        assertNull(cajero.getTurno());
    }

    @Test
    @DisplayName("Debería crear un cajero con el constructor vacío")
    public void testCrearCajeroConstructorVacio() {
        // Act
        Cajero cajero = new Cajero();

        // Assert
        assertNull(cajero.getId());
        assertNull(cajero.getNombre());
        assertNull(cajero.getCodigo());
        assertNull(cajero.getTurno());
    }

    @Test
    @DisplayName("Debería crear un cajero con el constructor completo")
    public void testCrearCajeroConstructorCompleto() {
        // Arrange
        Long id = 1L;
        String nombre = "Carlos Rodríguez";
        String codigo = "CAJ001";
        String turno = "Mañana";

        // Act
        Cajero cajero = new Cajero(id, nombre, codigo, turno);

        // Assert
        assertEquals(id, cajero.getId());
        assertEquals(nombre, cajero.getNombre());
        assertEquals(codigo, cajero.getCodigo());
        assertEquals(turno, cajero.getTurno());
    }

    @Test
    @DisplayName("Debería verificar la igualdad de dos cajeros")
    public void testEquals() {
        // Arrange
        Cajero cajero1 = Cajero.builder()
                .id(1L)
                .nombre("Carlos Rodríguez")
                .codigo("CAJ001")
                .turno("Mañana")
                .build();

        Cajero cajero2 = Cajero.builder()
                .id(1L)
                .nombre("Carlos Rodríguez")
                .codigo("CAJ001")
                .turno("Mañana")
                .build();

        Cajero cajeroDistinto = Cajero.builder()
                .id(2L)
                .nombre("María López")
                .codigo("CAJ002")
                .turno("Tarde")
                .build();

        // Assert
        assertEquals(cajero1, cajero2);
        assertNotEquals(cajero1, cajeroDistinto);
        assertNotEquals(cajero1, null);
        assertNotEquals(cajero1, new Object());
    }

    @Test
    @DisplayName("Debería generar el mismo hashCode para cajeros iguales")
    public void testHashCode() {
        // Arrange
        Cajero cajero1 = Cajero.builder()
                .id(1L)
                .nombre("Carlos Rodríguez")
                .codigo("CAJ001")
                .turno("Mañana")
                .build();

        Cajero cajero2 = Cajero.builder()
                .id(1L)
                .nombre("Carlos Rodríguez")
                .codigo("CAJ001")
                .turno("Mañana")
                .build();

        // Assert
        assertEquals(cajero1.hashCode(), cajero2.hashCode());
    }

    @Test
    @DisplayName("Debería generar una representación en String correcta")
    public void testToString() {
        // Arrange
        Cajero cajero = Cajero.builder()
                .id(1L)
                .nombre("Carlos Rodríguez")
                .codigo("CAJ001")
                .turno("Mañana")
                .build();

        // Act
        String toString = cajero.toString();

        // Assert
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nombre=Carlos Rodríguez"));
        assertTrue(toString.contains("codigo=CAJ001"));
        assertTrue(toString.contains("turno=Mañana"));
    }
}