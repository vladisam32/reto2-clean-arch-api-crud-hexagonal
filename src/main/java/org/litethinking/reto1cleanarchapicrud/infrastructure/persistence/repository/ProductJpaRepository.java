package org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.repository;

import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA pa' los productos, aquí hacemo' to' lo de la base de datos
 * Con esto guardamo', buscamo' y borramo' productos, bien fácil!
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    /**
     * Busca productos por nombre, pa' cuando el cliente pregunta
     * @param name el nombre que tamo' buscando
     * @return la lista de productos que coinciden, si no hay na' devuelve lista vacía
     */
    List<ProductJpaEntity> findByNameContainingIgnoreCase(String name);

    /**
     * Busca productos por categoría, pa' cuando queremo' ver to' lo de una categoría
     * @param category la categoría que tamo' buscando
     * @return la lista de productos de esa categoría
     */
    List<ProductJpaEntity> findByCategory(String category);

    /**
     * Busca productos que tán por debajo de un precio, pa' las ofertas
     * @param price el precio máximo
     * @return la lista de productos baratos
     */
    List<ProductJpaEntity> findByPriceLessThanEqual(Double price);

    /**
     * Busca un producto por su código de barra, pa' cuando lo escaneamo'
     * @param barcode el código de barra que tamo' buscando
     * @return el producto si lo encontramo', si no, na'
     */
    Optional<ProductJpaEntity> findByBarcode(String barcode);
}
