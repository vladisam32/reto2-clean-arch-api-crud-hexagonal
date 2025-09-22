package org.litethinking.supermercado.infrastructure.persistence.repository.inventario;

import org.litethinking.supermercado.infrastructure.entity.EntidadJpaProducto;
import org.litethinking.supermercado.infrastructure.entity.inventario.EntidadJpaInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for EntidadJpaInventario.
 */
@Repository
public interface RepositorioJpaInventario extends JpaRepository<EntidadJpaInventario, Long> {

    /**
     * Find an inventory record by its product.
     *
     * @param producto the product to search for
     * @return the inventory if found, empty otherwise
     */
    Optional<EntidadJpaInventario> findByProducto(EntidadJpaProducto producto);

    /**
     * Find inventory records with low stock.
     *
     * @return the list of inventory records with low stock
     */
    @Query("SELECT i FROM EntidadJpaInventario i WHERE i.cantidad <= i.stockMinimo")
    List<EntidadJpaInventario> findBajoStock();

    /**
     * Find inventory records by location.
     *
     * @param ubicacion the location to search for
     * @return the list of inventory records in the location
     */
    List<EntidadJpaInventario> findByUbicacion(String ubicacion);
}
