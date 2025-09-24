package org.litethinking.supermercado.restapi.config;


import org.litethinking.supermercado.application.ServicioCajero;
import org.litethinking.supermercado.application.ServicioCliente;
import org.litethinking.supermercado.application.ServicioProducto;
import org.litethinking.supermercado.application.service.inventario.ServicioInventario;

import org.litethinking.supermercado.application.mapper.InventarioMapper;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.litethinking.supermercado.shareddto.supermercado.ClienteDto;
import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class to initialize data from CSV files.
 */
@Configuration
public class DataInitializer {

    private final ServicioCajero servicioCajero;
    private final ServicioCliente servicioCliente;
    private final ServicioProducto servicioProducto;
    private final ServicioInventario servicioInventario;

    public DataInitializer(ServicioCajero servicioCajero, 
                          ServicioCliente servicioCliente,
                          ServicioProducto servicioProducto,
                          ServicioInventario servicioInventario) {
        this.servicioCajero = servicioCajero;
        this.servicioCliente = servicioCliente;
        this.servicioProducto = servicioProducto;
        this.servicioInventario = servicioInventario;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            loadCajeros();
            loadClientes();
            loadProductos();
            loadInventario();
        };
    }

    private void loadCajeros() {
        try {
            ClassPathResource resource = new ClassPathResource("data/cajeros.csv");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            // Skip header
            String line = reader.readLine();
            List<CajeroDto> cajeros = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                CajeroDto cajero = CajeroDto.builder()
                        .nombre(data[1])
                        .codigo(data[2])
                        .turno(data[3])
                        .build();
                cajeros.add(cajero);
            }

            reader.close();

            // Save all cajeros
            for (CajeroDto cajero : cajeros) {
                servicioCajero.crearCajero(cajero);
            }

            System.out.println("Loaded " + cajeros.size() + " cajeros from CSV file");
        } catch (Exception e) {
            System.err.println("Error loading cajeros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadClientes() {
        try {
            ClassPathResource resource = new ClassPathResource("data/clientes.csv");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            // Skip header
            String line = reader.readLine();
            List<ClienteDto> clientes = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                ClienteDto cliente = new ClienteDto(
                        null,
                        data[1],
                        data[2],
                        data[3],
                        data[4]
                );
                clientes.add(cliente);
            }

            reader.close();

            // Save all clientes
            for (ClienteDto cliente : clientes) {
                servicioCliente.crearCliente(cliente);
            }

            System.out.println("Loaded " + clientes.size() + " clientes from CSV file");
        } catch (Exception e) {
            System.err.println("Error loading clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadProductos() {
        try {
            ClassPathResource resource = new ClassPathResource("data/productos.csv");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            // Skip header
            String line = reader.readLine();
            List<ProductoDto> productos = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                ProductoDto producto = new ProductoDto(
                        null,
                        data[1],
                        data[2],
                        new BigDecimal(data[3]),
                        data[4],
                        data[5]
                );
                productos.add(producto);
            }

            reader.close();

            // Save all productos
            for (ProductoDto producto : productos) {
                servicioProducto.crearProducto(producto);
            }

            System.out.println("Loaded " + productos.size() + " productos from CSV file");
        } catch (Exception e) {
            System.err.println("Error loading productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadInventario() {
        try {
            ClassPathResource resource = new ClassPathResource("data/inventario.csv");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            // Skip header
            String line = reader.readLine();
            List<InventarioDto> inventarios = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Long productoId = Long.parseLong(data[1]);
                ProductoDto producto = servicioProducto.obtenerProductoPorId(productoId)
                        .orElseThrow(() -> new RuntimeException("Producto not found with id: " + productoId));

                InventarioDto inventario = new InventarioDto(
                        null,
                        producto,
                        Integer.parseInt(data[2]),
                        Integer.parseInt(data[3]),
                        Integer.parseInt(data[4]),
                        LocalDate.parse(data[5]),
                        data[6]
                );
                inventarios.add(inventario);
            }

            reader.close();

            // Save all inventarios
            for (InventarioDto inventario : inventarios) {
                servicioInventario.crearInventario(InventarioMapper.toDomain(inventario));
            }

            System.out.println("Loaded " + inventarios.size() + " inventarios from CSV file");
        } catch (Exception e) {
            System.err.println("Error loading inventarios: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
