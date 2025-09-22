package org.litethinking.supermercado.infrastructure.persistence.repository;

import org.litethinking.supermercado.infrastructure.entity.inventario.EntidadJpaInventario;
import org.litethinking.supermercado.infrastructure.entity.EntidadJpaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for EntidadJpaInventario.
 */
@Repository
public interface InventoryJpaRepository extends JpaRepository<EntidadJpaInventario, Long> {

    /**
     * Find inventory by product.
     * 
     * @param producto the product to search for
     * @return the inventory if found, empty otherwise
     */
    Optional<EntidadJpaInventario> findByProducto(EntidadJpaProducto producto);

    /**
     * Find inventory records with quantity less than or equal to minimum stock.
     * 
     * @return the list of inventory records with low stock
     */
    @Query("SELECT i FROM EntidadJpaInventario i WHERE i.cantidad <= i.stockMinimo")
    List<EntidadJpaInventario> findLowStock();

    /**
     * Find inventory records by location.
     * 
     * @param location the location to search for
     * @return the list of inventory records in the location
     */
    List<EntidadJpaInventario> findByUbicacion(String location);
}
