package org.litethinking.reto1cleanarchapicrud.domain.repository;

import org.litethinking.reto1cleanarchapicrud.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entity.
 */
public interface ProductRepository {
    
    /**
     * Save a product.
     *
     * @param product the product to save
     * @return the saved product
     */
    Product save(Product product);
    
    /**
     * Find a product by its id.
     *
     * @param id the id of the product
     * @return the product if found, empty otherwise
     */
    Optional<Product> findById(Long id);
    
    /**
     * Find all products.
     *
     * @return the list of products
     */
    List<Product> findAll();
    
    /**
     * Delete a product by its id.
     *
     * @param id the id of the product to delete
     */
    void deleteById(Long id);
    
    /**
     * Find products by category.
     *
     * @param category the category to search for
     * @return the list of products in the category
     */
    List<Product> findByCategory(String category);
    
    /**
     * Find a product by its barcode.
     *
     * @param barcode the barcode to search for
     * @return the product if found, empty otherwise
     */
    Optional<Product> findByBarcode(String barcode);
}