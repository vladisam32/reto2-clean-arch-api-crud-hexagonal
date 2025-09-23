package org.litethinking.supermercado.application.service.inventario;

import org.litethinking.supermercado.domain.model.Producto;
import org.litethinking.supermercado.domain.model.inventario.Inventario;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Inventario operations.
 */
public interface ServicioInventario {
    
    /**
     * Create a new inventory record.
     *
     * @param inventario the inventory to create
     * @return the created inventory
     */
    Inventario crearInventario(Inventario inventario);
    
    /**
     * Update an existing inventory record.
     *
     * @param id the id of the inventory to update
     * @param inventario the updated inventory data
     * @return the updated inventory
     */
    Inventario actualizarInventario(Long id, Inventario inventario);
    
    /**
     * Get an inventory record by its id.
     *
     * @param id the id of the inventory
     * @return the inventory if found, empty otherwise
     */
    Optional<Inventario> obtenerInventarioPorId(Long id);
    
    /**
     * Get all inventory records.
     *
     * @return the list of all inventory records
     */
    List<Inventario> obtenerTodoElInventario();
    
    /**
     * Delete an inventory record by its id.
     *
     * @param id the id of the inventory to delete
     */
    void eliminarInventario(Long id);
    
    /**
     * Get inventory record by product.
     *
     * @param producto the product to search for
     * @return the inventory if found, empty otherwise
     */
    Optional<Inventario> obtenerInventarioPorProducto(Producto producto);
    
    /**
     * Get inventory records with low stock.
     *
     * @return the list of inventory records with low stock
     */
    List<Inventario> obtenerInventarioConBajoStock();
    
    /**
     * Get inventory records by location.
     *
     * @param ubicacion the location to search for
     * @return the list of inventory records in the location
     */
    List<Inventario> obtenerInventarioPorUbicacion(String ubicacion);
    
    /**
     * Update inventory quantity.
     *
     * @param id the id of the inventory to update
     * @param cantidad the new quantity
     * @return the updated inventory
     */
    Inventario actualizarCantidadInventario(Long id, Integer cantidad);
}