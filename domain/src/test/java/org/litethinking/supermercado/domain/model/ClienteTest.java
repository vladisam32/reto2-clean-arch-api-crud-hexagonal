package org.litethinking.supermercado.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitario para la entidad Cliente.
 * Verifica el comportamiento de los métodos de la entidad.
 */
public class ClienteTest {

    @Test
    @DisplayName("Debería crear un cliente válido usando el builder")
    public void testCrearClienteValido() {
        // Arrange
        Long id = 1L;
        String nombre = "Juan Pérez";
        String email = "juan.perez@example.com";
        String telefono = "1234567890";
        String direccion = "Calle Principal 123";

        // Act
        Cliente cliente = Cliente.builder()
                .id(id)
                .nombre(nombre)
                .email(email)
                .telefono(telefono)
                .direccion(direccion)
                .build();

        // Assert
        assertEquals(id, cliente.getId());
        assertEquals(nombre, cliente.getNombre());
        assertEquals(email, cliente.getEmail());
        assertEquals(telefono, cliente.getTelefono());
        assertEquals(direccion, cliente.getDireccion());
    }

    @Test
    @DisplayName("Debería crear un cliente con valores nulos")
    public void testCrearClienteConValoresNulos() {
        // Act
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nombre(null)
                .email(null)
                .telefono(null)
                .direccion(null)
                .build();

        // Assert
        assertNull(cliente.getNombre());
        assertNull(cliente.getEmail());
        assertNull(cliente.getTelefono());
        assertNull(cliente.getDireccion());
    }

    @Test
    @DisplayName("Debería crear un cliente con el constructor vacío")
    public void testCrearClienteConstructorVacio() {
        // Act
        Cliente cliente = new Cliente();

        // Assert
        assertNull(cliente.getId());
        assertNull(cliente.getNombre());
        assertNull(cliente.getEmail());
        assertNull(cliente.getTelefono());
        assertNull(cliente.getDireccion());
    }

    @Test
    @DisplayName("Debería crear un cliente con el constructor completo")
    public void testCrearClienteConstructorCompleto() {
        // Arrange
        Long id = 1L;
        String nombre = "Juan Pérez";
        String email = "juan.perez@example.com";
        String telefono = "1234567890";
        String direccion = "Calle Principal 123";

        // Act
        Cliente cliente = new Cliente(id, nombre, email, telefono, direccion);

        // Assert
        assertEquals(id, cliente.getId());
        assertEquals(nombre, cliente.getNombre());
        assertEquals(email, cliente.getEmail());
        assertEquals(telefono, cliente.getTelefono());
        assertEquals(direccion, cliente.getDireccion());
    }

    @Test
    @DisplayName("Debería verificar la igualdad de dos clientes")
    public void testEquals() {
        // Arrange
        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .email("juan.perez@example.com")
                .telefono("1234567890")
                .direccion("Calle Principal 123")
                .build();

        Cliente cliente2 = Cliente.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .email("juan.perez@example.com")
                .telefono("1234567890")
                .direccion("Calle Principal 123")
                .build();

        Cliente clienteDistinto = Cliente.builder()
                .id(2L)
                .nombre("Ana López")
                .email("ana.lopez@example.com")
                .telefono("0987654321")
                .direccion("Avenida Secundaria 456")
                .build();

        // Assert
        assertEquals(cliente1, cliente2);
        assertNotEquals(cliente1, clienteDistinto);
        assertNotEquals(cliente1, null);
        assertNotEquals(cliente1, new Object());
    }

    @Test
    @DisplayName("Debería generar el mismo hashCode para clientes iguales")
    public void testHashCode() {
        // Arrange
        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .email("juan.perez@example.com")
                .telefono("1234567890")
                .direccion("Calle Principal 123")
                .build();

        Cliente cliente2 = Cliente.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .email("juan.perez@example.com")
                .telefono("1234567890")
                .direccion("Calle Principal 123")
                .build();

        // Assert
        assertEquals(cliente1.hashCode(), cliente2.hashCode());
    }

    @Test
    @DisplayName("Debería generar una representación en String correcta")
    public void testToString() {
        // Arrange
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .email("juan.perez@example.com")
                .telefono("1234567890")
                .direccion("Calle Principal 123")
                .build();

        // Act
        String toString = cliente.toString();

        // Assert
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nombre=Juan Pérez"));
        assertTrue(toString.contains("email=juan.perez@example.com"));
        assertTrue(toString.contains("telefono=1234567890"));
        assertTrue(toString.contains("direccion=Calle Principal 123"));
    }
}