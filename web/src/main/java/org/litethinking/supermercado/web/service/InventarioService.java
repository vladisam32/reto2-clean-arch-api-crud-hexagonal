package org.litethinking.supermercado.web.service;

import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service for interacting with the Inventory REST API.
 */
@Service
public class InventarioService {

    private final RestTemplate restTemplate;
    private final String apiBaseUrl;
    private final ProductoService productoService;

    @Autowired
    public InventarioService(RestTemplate restTemplate, String apiBaseUrl, ProductoService productoService) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
        this.productoService = productoService;
    }

    /**
     * Get all inventory items.
     *
     * @return a list of all inventory items
     */
    public List<InventarioDto> obtenerTodoElInventario() {
        try {
            ResponseEntity<List<InventarioDto>> response = restTemplate.exchange(
                    apiBaseUrl + "/inventario",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<InventarioDto>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Get an inventory item by its ID.
     *
     * @param id the ID of the inventory item to get
     * @return the inventory item if found, empty otherwise
     */
    public Optional<InventarioDto> obtenerInventarioPorId(Long id) {
        try {
            ResponseEntity<InventarioDto> response = restTemplate.getForEntity(
                    apiBaseUrl + "/inventario/" + id,
                    InventarioDto.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Get an inventory item by product ID.
     *
     * @param productoId the ID of the product
     * @return the inventory item if found, empty otherwise
     */
    public Optional<InventarioDto> obtenerInventarioPorProductoId(Long productoId) {
        try {
            ResponseEntity<InventarioDto> response = restTemplate.getForEntity(
                    apiBaseUrl + "/inventario/producto/" + productoId,
                    InventarioDto.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Create a new inventory item.
     *
     * @param inventarioDto the inventory item to create
     * @return the created inventory item
     */
    public InventarioDto crearInventario(InventarioDto inventarioDto) {
        return restTemplate.postForObject(
                apiBaseUrl + "/inventario",
                inventarioDto,
                InventarioDto.class
        );
    }

    /**
     * Update an existing inventory item.
     *
     * @param id the ID of the inventory item to update
     * @param inventarioDto the updated inventory item data
     */
    public void actualizarInventario(Long id, InventarioDto inventarioDto) {
        restTemplate.put(
                apiBaseUrl + "/inventario/" + id,
                inventarioDto
        );
    }

    /**
     * Delete an inventory item.
     *
     * @param id the ID of the inventory item to delete
     */
    public void eliminarInventario(Long id) {
        restTemplate.delete(apiBaseUrl + "/inventario/" + id);
    }

    /**
     * Get all products that are not in inventory.
     *
     * @return a list of products not in inventory
     */
    public List<ProductoDto> obtenerProductosSinInventario() {
        List<ProductoDto> todosLosProductos = productoService.obtenerTodosLosProductos();
        List<InventarioDto> todoElInventario = obtenerTodoElInventario();
        
        // Remove products that are already in inventory
        todosLosProductos.removeIf(producto -> 
            todoElInventario.stream()
                .anyMatch(inv -> inv.producto().id().equals(producto.id()))
        );
        
        return todosLosProductos;
    }
}