package org.litethinking.supermercado.domain.ports.output;

import org.litethinking.supermercado.domain.model.Producto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Puerto secundario (salida) para operaciones de persistencia de Producto.
 * Define las operaciones que cualquier adaptador de persistencia debe implementar.
 */
public interface RepositorioProductoPort {

    /**
     * Guarda un producto.
     *
     * @param producto el producto a guardar
     * @return el producto guardado
     */
    Producto save(Producto producto);

    /**
     * Busca un producto por su id.
     *
     * @param id el id del producto
     * @return el producto si se encuentra, vacío en caso contrario
     */
    Optional<Producto> findById(Long id);

    /**
     * Busca todos los productos.
     *
     * @return la lista de productos
     */
    List<Producto> findAll();

    /**
     * Elimina un producto por su id.
     *
     * @param id el id del producto a eliminar
     */
    void deleteById(Long id);

    /**
     * Busca productos por categoría.
     *
     * @param categoria la categoría a buscar
     * @return la lista de productos en la categoría
     */
    List<Producto> findByCategoria(String categoria);

    /**
     * Busca productos por rango de precio.
     *
     * @param precioMinimo el precio mínimo
     * @param precioMaximo el precio máximo
     * @return la lista de productos en el rango de precio
     */
    List<Producto> findByPrecioBetween(BigDecimal precioMinimo, BigDecimal precioMaximo);

    /**
     * Busca un producto por su código de barras.
     *
     * @param codigoBarras el código de barras a buscar
     * @return el producto si se encuentra, vacío en caso contrario
     */
    Optional<Producto> findByCodigoBarras(String codigoBarras);
}
