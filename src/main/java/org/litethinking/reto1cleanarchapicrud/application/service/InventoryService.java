package org.litethinking.reto1cleanarchapicrud.application.service;

import org.litethinking.reto1cleanarchapicrud.domain.model.Inventory;
import org.litethinking.reto1cleanarchapicrud.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing inventory.
 */
public interface InventoryService {
    
    /**
     * Create a new inventory record.
     *
     * @param inventory the inventory to create
     * @return the created inventory
     */
    Inventory createInventory(Inventory inventory);
    
    /**
     * Update an existing inventory record.
     *
     * @param id the id of the inventory to update
     * @param inventory the updated inventory data
     * @return the updated inventory
     * @throws RuntimeException if the inventory is not found
     */
    Inventory updateInventory(Long id, Inventory inventory);
    
    /**
     * Get an inventory record by its id.
     *
     * @param id the id of the inventory
     * @return the inventory if found, empty otherwise
     */
    Optional<Inventory> getInventoryById(Long id);
    
    /**
     * Get all inventory records.
     *
     * @return the list of all inventory records
     */
    List<Inventory> getAllInventory();
    
    /**
     * Delete an inventory record by its id.
     *
     * @param id the id of the inventory to delete
     * @throws RuntimeException if the inventory is not found
     */
    void deleteInventory(Long id);
    
    /**
     * Get inventory by product.
     *
     * @param product the product to search for
     * @return the inventory record if found, empty otherwise
     */
    Optional<Inventory> getInventoryByProduct(Product product);
    
    /**
     * Get inventory records with quantity below minimum stock.
     *
     * @return the list of inventory records with low stock
     */
    List<Inventory> getLowStockInventory();
    
    /**
     * Get inventory records by location.
     *
     * @param location the location to search for
     * @return the list of inventory records in the location
     */
    List<Inventory> getInventoryByLocation(String location);
    
    /**
     * Update inventory quantity.
     *
     * @param id the id of the inventory to update
     * @param quantity the new quantity
     * @return the updated inventory
     * @throws RuntimeException if the inventory is not found
     */
    Inventory updateInventoryQuantity(Long id, Integer quantity);
}