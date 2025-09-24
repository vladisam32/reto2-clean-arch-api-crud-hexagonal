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
public interface RepositorioJpaProducto extends JpaRepository<EntidadJpaProducto, Long> {

    /**
     * Find products by category.
     *
     * @param categoria the category to search for
     * @return the list of products in the category
     */
    List<EntidadJpaProducto> findByCategoria(String categoria);

    /**
     * Find products by price range.
     *
     * @param precioMinimo the minimum price
     * @param precioMaximo the maximum price
     * @return the list of products in the price range
     */
    List<EntidadJpaProducto> findByPrecioBetween(BigDecimal precioMinimo, BigDecimal precioMaximo);

    /**
     * Find a product by its barcode.
     *
     * @param codigoBarras the barcode to search for
     * @return the product if found, empty otherwise
     */
    Optional<EntidadJpaProducto> findByCodigoBarras(String codigoBarras);
}
