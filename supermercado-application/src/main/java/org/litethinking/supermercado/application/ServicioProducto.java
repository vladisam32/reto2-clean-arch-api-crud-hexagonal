package org.litethinking.supermercado.application;

import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Producto operations.
 */
public interface ServicioProducto {

    /**
     * Create a new product.
     *
     * @param productoDto the product to create
     * @return the created product
     */
    ProductoDto crearProducto(ProductoDto productoDto);

    /**
     * Update an existing product.
     *
     * @param id the id of the product to update
     * @param productoDto the updated product data
     * @return the updated product
     */
    ProductoDto actualizarProducto(Long id, ProductoDto productoDto);

    /**
     * Get a product by its id.
     *
     * @param id the id of the product
     * @return the product if found, empty otherwise
     */
    Optional<ProductoDto> obtenerProductoPorId(Long id);

    /**
     * Get all products.
     *
     * @return the list of all products
     */
    List<ProductoDto> obtenerTodosLosProductos();

    /**
     * Delete a product by its id.
     *
     * @param id the id of the product to delete
     */
    void eliminarProducto(Long id);

    /**
     * Get products by category.
     *
     * @param categoria the category to search for
     * @return the list of products in the category
     */
    List<ProductoDto> obtenerProductosPorCategoria(String categoria);

    /**
     * Get products by price range.
     *
     * @param precioMinimo the minimum price
     * @param precioMaximo the maximum price
     * @return the list of products in the price range
     */
    List<ProductoDto> obtenerProductosPorRangoDePrecio(BigDecimal precioMinimo, BigDecimal precioMaximo);

    /**
     * Get a product by its barcode.
     *
     * @param codigoBarras the barcode to search for
     * @return the product if found, empty otherwise
     */
    Optional<ProductoDto> obtenerProductoPorCodigoBarras(String codigoBarras);
}
