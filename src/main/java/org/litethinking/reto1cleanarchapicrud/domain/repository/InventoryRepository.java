package org.litethinking.reto1cleanarchapicrud.domain.repository;

import org.litethinking.reto1cleanarchapicrud.domain.model.Inventory;
import org.litethinking.reto1cleanarchapicrud.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Inventory entity.
 */
public interface InventoryRepository {
    
    /**
     * Save an inventory record.
     *
     * @param inventory the inventory to save
     * @return the saved inventory
     */
    Inventory save(Inventory inventory);
    
    /**
     * Find an inventory record by its id.
     *
     * @param id the id of the inventory
     * @return the inventory if found, empty otherwise
     */
    Optional<Inventory> findById(Long id);
    
    /**
     * Find all inventory records.
     *
     * @return the list of inventory records
     */
    List<Inventory> findAll();
    
    /**
     * Delete an inventory record by its id.
     *
     * @param id the id of the inventory to delete
     */
    void deleteById(Long id);
    
    /**
     * Find inventory by product.
     *
     * @param product the product to search for
     * @return the inventory record if found, empty otherwise
     */
    Optional<Inventory> findByProduct(Product product);
    
    /**
     * Find inventory records with quantity below minimum stock.
     *
     * @return the list of inventory records with low stock
     */
    List<Inventory> findLowStock();
    
    /**
     * Find inventory records by location.
     *
     * @param location the location to search for
     * @return the list of inventory records in the location
     */
    List<Inventory> findByLocation(String location);
}