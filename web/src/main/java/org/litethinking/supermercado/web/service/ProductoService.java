package org.litethinking.supermercado.web.service;

import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service for interacting with the Product REST API.
 */
@Service
public class ProductoService {

    private final RestTemplate restTemplate;
    private final String apiBaseUrl;

    @Autowired
    public ProductoService(RestTemplate restTemplate, String apiBaseUrl) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
    }

    /**
     * Get all products.
     *
     * @return a list of all products
     */
    public List<ProductoDto> obtenerTodosLosProductos() {
        try {
            ResponseEntity<List<ProductoDto>> response = restTemplate.exchange(
                    apiBaseUrl + "/productos",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProductoDto>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Get a product by its ID.
     *
     * @param id the ID of the product to get
     * @return the product if found, empty otherwise
     */
    public Optional<ProductoDto> obtenerProductoPorId(Long id) {
        try {
            ResponseEntity<ProductoDto> response = restTemplate.getForEntity(
                    apiBaseUrl + "/productos/" + id,
                    ProductoDto.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Create a new product.
     *
     * @param productoDto the product to create
     * @return the created product
     */
    public ProductoDto crearProducto(ProductoDto productoDto) {
        return restTemplate.postForObject(
                apiBaseUrl + "/productos",
                productoDto,
                ProductoDto.class
        );
    }

    /**
     * Update an existing product.
     *
     * @param id the ID of the product to update
     * @param productoDto the updated product data
     */
    public void actualizarProducto(Long id, ProductoDto productoDto) {
        restTemplate.put(
                apiBaseUrl + "/productos/" + id,
                productoDto
        );
    }

    /**
     * Delete a product.
     *
     * @param id the ID of the product to delete
     */
    public void eliminarProducto(Long id) {
        restTemplate.delete(apiBaseUrl + "/productos/" + id);
    }

    /**
     * Get products by category.
     *
     * @param categoria the category to search for
     * @return a list of products in the category
     */
    public List<ProductoDto> obtenerProductosPorCategoria(String categoria) {
        try {
            ResponseEntity<List<ProductoDto>> response = restTemplate.exchange(
                    apiBaseUrl + "/productos/categoria/" + categoria,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProductoDto>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}