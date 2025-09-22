package org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.repository;

import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.entity.InventoryJpaEntity;
import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA pa' el inventario, aquí controlamo' to' lo que hay en la tienda
 * Con esto sabemo' qué tenemo', qué falta y dónde tá to'
 */
@Repository
public interface InventoryJpaRepository extends JpaRepository<InventoryJpaEntity, Long> {

    /**
     * Busca el inventario de un producto, pa' saber cuánto queda
     * @param product el producto que tamo' buscando
     * @return el inventario del producto, si existe
     */
    Optional<InventoryJpaEntity> findByProduct(ProductJpaEntity product);

    /**
     * Busca inventario por ubicación, pa' saber qué hay en cada parte
     * @param location la ubicación que tamo' buscando
     * @return la lista de inventario en esa ubicación
     */
    List<InventoryJpaEntity> findByLocation(String location);

    /**
     * Busca productos con poco inventario, pa' saber qué hay que comprar
     * @return la lista de inventario bajo
     */
    @Query("SELECT i FROM InventoryJpaEntity i WHERE i.quantity <= i.minimumStock")
    List<InventoryJpaEntity> findLowStock();

    /**
     * Busca productos con mucho inventario, pa' las promociones
     * @return la lista de inventario alto
     */
    @Query("SELECT i FROM InventoryJpaEntity i WHERE i.quantity >= i.maximumStock")
    List<InventoryJpaEntity> findExcessStock();
}
