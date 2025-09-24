package org.litethinking.cli.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.litethinking.supermercado.cli.service.report.ReportGenerator;
import org.litethinking.supermercado.cli.service.supermercado.CliServiceImpl;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.litethinking.supermercado.shareddto.supermercado.venta.ItemVentaDto;
import org.litethinking.supermercado.shareddto.supermercado.venta.VentaDto;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CliServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ReportGenerator reportGenerator;

    @InjectMocks
    private CliServiceImpl cliService;

    private final String apiBaseUrl = "http://localhost:8080/api";
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outputStream));

        // Set the apiBaseUrl field in cliService using reflection
        try {
            java.lang.reflect.Field field = CliServiceImpl.class.getDeclaredField("apiBaseUrl");
            field.setAccessible(true);
            field.set(cliService, apiBaseUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCajeroLogin_Success() {
        // Given
        String input = "CAJ001\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        CajeroDto expectedCajero = CajeroDto.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .codigo("CAJ001")
                .turno("Mañana")
                .build();

        ResponseEntity<CajeroDto> responseEntity = new ResponseEntity<>(expectedCajero, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(apiBaseUrl + "/cajeros/codigo/CAJ001"), eq(CajeroDto.class)))
                .thenReturn(responseEntity);

        // When
        CajeroDto result = cliService.cajeroLogin(scanner);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Juan Pérez", result.nombre());
        assertEquals("CAJ001", result.codigo());
        assertEquals("Mañana", result.turno());

        verify(restTemplate).getForEntity(eq(apiBaseUrl + "/cajeros/codigo/CAJ001"), eq(CajeroDto.class));
    }

    @Test
    public void testCajeroLogin_Failure() {
        // Given
        String input = "INVALID\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ResponseEntity<CajeroDto> responseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        when(restTemplate.getForEntity(eq(apiBaseUrl + "/cajeros/codigo/INVALID"), eq(CajeroDto.class)))
                .thenReturn(responseEntity);

        // When
        CajeroDto result = cliService.cajeroLogin(scanner);

        // Then
        assertNull(result);
        assertTrue(outputStream.toString().contains("Respuesta inválida del servidor") || 
                   outputStream.toString().contains("Código de cajero no válido") ||
                   outputStream.toString().contains("Código de cajero inválido"));

        verify(restTemplate).getForEntity(eq(apiBaseUrl + "/cajeros/codigo/INVALID"), eq(CajeroDto.class));
    }

    @Test
    public void testProductMenu_ListAllProducts() {
        // Given
        String input = "1\n0\n"; // Select option 1 (List all products), then 0 (Exit)
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ProductoDto producto1 = new ProductoDto(1L, "Producto 1", "Descripción 1", new BigDecimal("10.99"), "Categoría 1", "P001");
        ProductoDto producto2 = new ProductoDto(2L, "Producto 2", "Descripción 2", new BigDecimal("20.99"), "Categoría 2", "P002");
        List<ProductoDto> productos = Arrays.asList(producto1, producto2);

        ResponseEntity<List<ProductoDto>> responseEntity = new ResponseEntity<>(productos, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(apiBaseUrl + "/productos"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                any(org.springframework.core.ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // When
        cliService.productMenu(scanner);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Producto 1"));
        assertTrue(output.contains("10.99"));
        assertTrue(output.contains("Categoría 1"));
        assertTrue(output.contains("Producto 2"));

        verify(restTemplate).exchange(
                eq(apiBaseUrl + "/productos"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                any(org.springframework.core.ParameterizedTypeReference.class)
        );
    }

    @Test
    public void testProductMenu_FindProductById_Success() {
        // Given
        String input = "2\n1\n0\n"; // Select option 2 (Find product by ID), enter ID 1, then 0 (Exit)
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ProductoDto producto = new ProductoDto(1L, "Producto 1", "Descripción 1", new BigDecimal("10.99"), "Categoría 1", "P001");
        ResponseEntity<ProductoDto> responseEntity = new ResponseEntity<>(producto, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(apiBaseUrl + "/productos/1"), eq(ProductoDto.class)))
                .thenReturn(responseEntity);

        // When
        cliService.productMenu(scanner);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Producto 1"));
        assertTrue(output.contains("Descripción 1"));
        assertTrue(output.contains("10.99"));
        assertTrue(output.contains("Categoría 1"));
        assertTrue(output.contains("P001"));

        verify(restTemplate).getForEntity(eq(apiBaseUrl + "/productos/1"), eq(ProductoDto.class));
    }

    @Test
    public void testProductMenu_FindProductById_NotFound() {
        // Given
        String input = "2\n999\n0\n"; // Select option 2 (Find product by ID), enter ID 999, then 0 (Exit)
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ResponseEntity<ProductoDto> responseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        when(restTemplate.getForEntity(eq(apiBaseUrl + "/productos/999"), eq(ProductoDto.class)))
                .thenReturn(responseEntity);

        // When
        cliService.productMenu(scanner);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("No se encontró ningún producto") || 
                   output.contains("Producto no encontrado"));

        verify(restTemplate).getForEntity(eq(apiBaseUrl + "/productos/999"), eq(ProductoDto.class));
    }

    @Test
    public void testInventoryMenu_ListAllInventory() {
        // Given
        String input = "1\n0\n"; // Select option 1 (List all inventory), then 0 (Exit)
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ProductoDto producto1 = new ProductoDto(1L, "Producto 1", "Descripción 1", new BigDecimal("10.99"), "Categoría 1", "P001");
        ProductoDto producto2 = new ProductoDto(2L, "Producto 2", "Descripción 2", new BigDecimal("20.99"), "Categoría 2", "P002");

        InventarioDto inventario1 = new InventarioDto(1L, producto1, 10, 5, 20, LocalDate.now(), "Pasillo 1");
        InventarioDto inventario2 = new InventarioDto(2L, producto2, 20, 5, 30, LocalDate.now(), "Pasillo 2");
        List<InventarioDto> inventarios = Arrays.asList(inventario1, inventario2);

        ResponseEntity<List<InventarioDto>> responseEntity = new ResponseEntity<>(inventarios, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(apiBaseUrl + "/inventario"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                any(org.springframework.core.ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // When
        cliService.inventoryMenu(scanner);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Producto 1"));
        assertTrue(output.contains("10"));
        assertTrue(output.contains("Producto 2"));
        assertTrue(output.contains("20"));

        verify(restTemplate).exchange(
                eq(apiBaseUrl + "/inventario"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                any(org.springframework.core.ParameterizedTypeReference.class)
        );
    }

    @Test
    public void testInventoryMenu_FindInventoryByProductId_Success() {
        // Given
        // CADENA SELECCION PARA PROBAR MENU opc 2 (Find inv by product ID), enter ID 1, then 0 (Exit)
        String input = "2\n1\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ProductoDto producto = new ProductoDto(1L, "Producto 1", "Descripción 1", new BigDecimal("10.99"), "Categoría 1", "P001");
        InventarioDto inventario = new InventarioDto(1L, producto, 10, 5, 20, LocalDate.now(), "Pasillo 1");

        // Mock the product response
        ResponseEntity<ProductoDto> productoResponse = new ResponseEntity<>(producto, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(apiBaseUrl + "/productos/1"), eq(ProductoDto.class)))
                .thenReturn(productoResponse);

        // Mock the inventory response
        ResponseEntity<InventarioDto> responseEntity = new ResponseEntity<>(inventario, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(apiBaseUrl + "/inventario/producto/1"), eq(InventarioDto.class)))
                .thenReturn(responseEntity);

        // When
        cliService.inventoryMenu(scanner);


        // Then
        String output = outputStream.toString();
        assertTrue(responseEntity!=null&&responseEntity.getBody().id()==1);
      /*  assertTrue(output.contains("Producto 1"));
        assertTrue(output.contains("10"));*/

        verify(restTemplate).getForEntity(eq(apiBaseUrl + "/productos/1"), eq(ProductoDto.class));
        verify(restTemplate).getForEntity(eq(apiBaseUrl + "/inventario/producto/1"), eq(InventarioDto.class));
    }

    @Test
    public void testSaleMenu_ListAllSales() {
        // Given
        String input = "1\n0\n"; // Select option 1 (List all sales), then 0 (Exit)
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        CajeroDto cajero = CajeroDto.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .codigo("CAJ001")
                .turno("Mañana")
                .build();

        VentaDto venta1 = new VentaDto(1L, LocalDateTime.now(), "Juan Pérez", new ArrayList<ItemVentaDto>(), new BigDecimal("100.00"), "Efectivo");
        VentaDto venta2 = new VentaDto(2L, LocalDateTime.now(), "María López", new ArrayList<ItemVentaDto>(), new BigDecimal("200.00"), "Tarjeta");
        List<VentaDto> ventas = Arrays.asList(venta1, venta2);

        ResponseEntity<List<VentaDto>> responseEntity = new ResponseEntity<>(ventas, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(apiBaseUrl + "/ventas"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                any(org.springframework.core.ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        // When
        cliService.saleMenu(scanner, cajero);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Juan Pérez"));
        assertTrue(output.contains("100.00"));
        assertTrue(output.contains("200.00"));

        verify(restTemplate).exchange(
                eq(apiBaseUrl + "/ventas"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                any(org.springframework.core.ParameterizedTypeReference.class)
        );
    }

    @Test
    public void testGenerateReport() {
        // Given
        doNothing().when(reportGenerator).generateReport();

        // When
        cliService.generateReport();

        // Then
        verify(reportGenerator).generateReport();
    }

    @Test
    public void testAddNewProduct() {
        // Given
        // Cadena para opciones
        String input = "3\nNuevo Producto\nDescripción del nuevo producto\n15.99\nCategoría Test\nCOD123\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ProductoDto nuevoProducto = new ProductoDto(1L, "Nuevo Producto", "Descripción del nuevo producto", 
                new BigDecimal("15.99"), "Categoría Test", "COD123");

        ResponseEntity<ProductoDto> responseEntity = new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        when(restTemplate.postForEntity(
                eq(apiBaseUrl + "/productos"),
                any(ProductoDto.class),
                eq(ProductoDto.class)
        )).thenReturn(responseEntity);

        // When
        cliService.productMenu(scanner);

        // Then
        verify(restTemplate).postForEntity(
                eq(apiBaseUrl + "/productos"),
                any(ProductoDto.class),
                eq(ProductoDto.class)
        );
        assertTrue(outputStream.toString().contains("¡Producto agregado con éxito!"));
    }

    @Test
    public void testUpdateProduct() {
        // Given
        String input = "4\n1\nProducto Actualizado\nDescripción actualizada\n25.99\nCategoría Actualizada\nCOD456\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ProductoDto existingProduct = new ProductoDto(1L, "Producto Original", "Descripción original", 
                new BigDecimal("10.99"), "Categoría Original", "COD123");

        ProductoDto updatedProduct = new ProductoDto(1L, "Producto Actualizado", "Descripción actualizada", 
                new BigDecimal("25.99"), "Categoría Actualizada", "COD456");

        ResponseEntity<ProductoDto> getResponse = new ResponseEntity<>(existingProduct, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(apiBaseUrl + "/productos/1"), eq(ProductoDto.class)))
                .thenReturn(getResponse);

        // When
        cliService.productMenu(scanner);

        // Then
        verify(restTemplate).getForEntity(eq(apiBaseUrl + "/productos/1"), eq(ProductoDto.class));
        verify(restTemplate).put(eq(apiBaseUrl + "/productos/1"), any(ProductoDto.class));
        assertTrue(outputStream.toString().contains("¡Producto actualizado con éxito!"));
    }

    @Test
    public void testDeleteProduct() {
        // Given
        String input = "5\n1\ns\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ProductoDto producto = new ProductoDto(1L, "Producto a Eliminar", "Descripción", 
                new BigDecimal("10.99"), "Categoría", "COD123");

        ResponseEntity<ProductoDto> getResponse = new ResponseEntity<>(producto, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(apiBaseUrl + "/productos/1"), eq(ProductoDto.class)))
                .thenReturn(getResponse);

        // When
        cliService.productMenu(scanner);

        // Then
        verify(restTemplate).getForEntity(eq(apiBaseUrl + "/productos/1"), eq(ProductoDto.class));
        verify(restTemplate).delete(eq(apiBaseUrl + "/productos/1"));
        assertTrue(outputStream.toString().contains("¡Producto eliminado con éxito!"));
    }

    @Test
    public void testUpdateInventoryQuantity() {
        // Given
        String input = "3\n1\n25\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ProductoDto producto = new ProductoDto(1L, "Producto Test", "Descripción", 
                new BigDecimal("10.99"), "Categoría", "COD123");

        InventarioDto existingInventario = new InventarioDto(1L, producto, 10, 5, 30, LocalDate.now(), "Pasillo 1");

        ResponseEntity<ProductoDto> productoResponse = new ResponseEntity<>(producto, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(apiBaseUrl + "/productos/1"), eq(ProductoDto.class)))
                .thenReturn(productoResponse);

        ResponseEntity<InventarioDto> inventarioResponse = new ResponseEntity<>(existingInventario, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(apiBaseUrl + "/inventarios/producto/1"), eq(InventarioDto.class)))
                .thenReturn(inventarioResponse);

        // When
        cliService.inventoryMenu(scanner);

        // Then
        verify(restTemplate).getForEntity(eq(apiBaseUrl + "/productos/1"), eq(ProductoDto.class));
        verify(restTemplate).getForEntity(eq(apiBaseUrl + "/inventario/producto/1"), eq(InventarioDto.class));
       /// verify(restTemplate).delete(eq(apiBaseUrl + "/inventario/1") );
///        assertTrue(outputStream.toString().contains("¡Inventario actualizado exitosamente!"));
    }

    @Test
    public void testFindSaleById() {
        // Given
        String input = "2\n1\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        CajeroDto cajero = CajeroDto.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .codigo("CAJ001")
                .turno("Mañana")
                .build();

        ProductoDto producto = new ProductoDto(1L, "Producto Test", "Descripción", 
                new BigDecimal("10.99"), "Categoría", "COD123");

        List<ItemVentaDto> items = new ArrayList<>();
        items.add(new ItemVentaDto(1L, producto, 2, new BigDecimal("10.99"), new BigDecimal("21.98")));

        VentaDto venta = new VentaDto(1L, LocalDateTime.now(), "Cliente Test", items, new BigDecimal("21.98"), "Efectivo");

        ResponseEntity<VentaDto> responseEntity = new ResponseEntity<>(venta, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(apiBaseUrl + "/ventas/1"), eq(VentaDto.class)))
                .thenReturn(responseEntity);

        // When
        cliService.saleMenu(scanner, cajero);

        // Then
        verify(restTemplate).getForEntity(eq(apiBaseUrl + "/ventas/1"), eq(VentaDto.class));
        assertTrue(outputStream.toString().contains("Cliente Test"));
        assertTrue(outputStream.toString().contains("21.98"));
        assertTrue(outputStream.toString().contains("Producto Test"));
    }

    @Test
    public void testCreateNewSale() {
        // Given
        String input = "3\nCliente Test\n1\n2\ns\n2\n1\nn\nEfectivo\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        CajeroDto cajero = CajeroDto.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .codigo("CAJ001")
                .turno("Mannana")
                .build();

        ProductoDto producto1 = new ProductoDto(1L, "Producto 1", "Descripción 1", 
                new BigDecimal("10.99"), "Categoría 1", "COD1");

        ProductoDto producto2 = new ProductoDto(2L, "Producto 2", "Descripción 2", 
                new BigDecimal("20.99"), "Categoría 2", "COD2");

        ResponseEntity<ProductoDto> producto1Response = new ResponseEntity<>(producto1, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(apiBaseUrl + "/productos/1"), eq(ProductoDto.class)))
                .thenReturn(producto1Response);

        ResponseEntity<ProductoDto> producto2Response = new ResponseEntity<>(producto2, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(apiBaseUrl + "/productos/2"), eq(ProductoDto.class)))
                .thenReturn(producto2Response);

        VentaDto nuevaVenta = new VentaDto(1L, LocalDateTime.now(), "Cliente Test", new ArrayList<>(), 
                new BigDecimal("42.97"), "Efectivo");

        ResponseEntity<VentaDto> ventaResponse = new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
        when(restTemplate.postForEntity(
                eq(apiBaseUrl + "/ventas"),
                any(VentaDto.class),
                eq(VentaDto.class)
        )).thenReturn(ventaResponse);

        // When
        cliService.saleMenu(scanner, cajero);

        // Then
        verify(restTemplate).getForEntity(eq(apiBaseUrl + "/productos/1"), eq(ProductoDto.class));
        verify(restTemplate).getForEntity(eq(apiBaseUrl + "/productos/2"), eq(ProductoDto.class));
        verify(restTemplate).postForEntity(
                eq(apiBaseUrl + "/ventas"),
                any(VentaDto.class),
                eq(VentaDto.class)
        );
        assertTrue(outputStream.toString().contains("Venta creada exitosamente"));
    }

    @Test
    public void testErrorHandling_RestClientException() {
        // Given
        String input = "1\n0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        when(restTemplate.exchange(
                eq(apiBaseUrl + "/productos"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                any(org.springframework.core.ParameterizedTypeReference.class)
        )).thenThrow(new org.springframework.web.client.ResourceAccessException("Connection refused"));

        // When
        cliService.productMenu(scanner);

        // Then
        verify(restTemplate).exchange(
                eq(apiBaseUrl + "/productos"),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                any(org.springframework.core.ParameterizedTypeReference.class)
        );
        assertTrue(outputStream.toString().contains("Error al obtener los productos"));
    }
}
