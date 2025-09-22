package org.litethinking.reto1cleanarchapicrud.application.service;

import org.litethinking.reto1cleanarchapicrud.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing products.
 */
public interface ProductService {
    
    /**
     * Create a new product.
     *
     * @param product the product to create
     * @return the created product
     */
    Product createProduct(Product product);
    
    /**
     * Update an existing product.
     *
     * @param id the id of the product to update
     * @param product the updated product data
     * @return the updated product
     * @throws RuntimeException if the product is not found
     */
    Product updateProduct(Long id, Product product);
    
    /**
     * Get a product by its id.
     *
     * @param id the id of the product
     * @return the product if found, empty otherwise
     */
    Optional<Product> getProductById(Long id);
    
    /**
     * Get all products.
     *
     * @return the list of all products
     */
    List<Product> getAllProducts();
    
    /**
     * Delete a product by its id.
     *
     * @param id the id of the product to delete
     * @throws RuntimeException if the product is not found
     */
    void deleteProduct(Long id);
    
    /**
     * Get products by category.
     *
     * @param category the category to search for
     * @return the list of products in the category
     */
    List<Product> getProductsByCategory(String category);
    
    /**
     * Get a product by its barcode.
     *
     * @param barcode the barcode to search for
     * @return the product if found, empty otherwise
     */
    Optional<Product> getProductByBarcode(String barcode);
}