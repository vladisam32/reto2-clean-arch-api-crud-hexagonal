package org.litethinking.supermercado.infrastructure.persistence.repository;

import org.litethinking.supermercado.infrastructure.entity.EntidadJpaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * JPA repository for EntidadJpaProducto.
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<EntidadJpaProducto, Long> {

    /**
     * Find products by name containing the given string (case insensitive).
     * 
     * @param nombre the name to search for
     * @return the list of products matching the name
     */
    List<EntidadJpaProducto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Find products by category.
     * 
     * @param categoria the category to search for
     * @return the list of products in the category
     */
    List<EntidadJpaProducto> findByCategoria(String categoria);

    /**
     * Find products with price less than or equal to the given price.
     * 
     * @param precio the maximum price
     * @return the list of products with price less than or equal to the given price
     */
    List<EntidadJpaProducto> findByPrecioLessThanEqual(BigDecimal precio);

    /**
     * Find a product by its barcode.
     * 
     * @param codigoBarras the barcode to search for
     * @return the product if found, empty otherwise
     */
    Optional<EntidadJpaProducto> findByCodigoBarras(String codigoBarras);
}
