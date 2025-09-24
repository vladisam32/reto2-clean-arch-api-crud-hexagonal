package org.litethinking.supermercado.domain.ports.input;

import org.litethinking.supermercado.domain.model.Producto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Puerto primario (entrada) para operaciones de negocio relacionadas con Producto.
 * Define los casos de uso que la aplicación soporta para productos.
 */
public interface ServicioProductoPort {
    
    /**
     * Crea un nuevo producto.
     *
     * @param producto el producto a crear
     * @return el producto creado
     */
    Producto crearProducto(Producto producto);
    
    /**
     * Actualiza un producto existente.
     *
     * @param id el id del producto a actualizar
     * @param producto los datos actualizados del producto
     * @return el producto actualizado
     * @throws IllegalArgumentException si el producto no existe
     */
    Producto actualizarProducto(Long id, Producto producto);
    
    /**
     * Obtiene un producto por su id.
     *
     * @param id el id del producto
     * @return el producto si se encuentra, vacío en caso contrario
     */
    Optional<Producto> obtenerProductoPorId(Long id);
    
    /**
     * Obtiene todos los productos.
     *
     * @return la lista de todos los productos
     */
    List<Producto> obtenerTodosLosProductos();
    
    /**
     * Elimina un producto por su id.
     *
     * @param id el id del producto a eliminar
     * @throws IllegalArgumentException si el producto no existe
     */
    void eliminarProducto(Long id);
    
    /**
     * Obtiene productos por categoría.
     *
     * @param categoria la categoría a buscar
     * @return la lista de productos en la categoría
     */
    List<Producto> obtenerProductosPorCategoria(String categoria);
    
    /**
     * Obtiene productos por rango de precio.
     *
     * @param precioMinimo el precio mínimo
     * @param precioMaximo el precio máximo
     * @return la lista de productos en el rango de precio
     */
    List<Producto> obtenerProductosPorRangoDePrecio(BigDecimal precioMinimo, BigDecimal precioMaximo);
    
    /**
     * Obtiene un producto por su código de barras.
     *
     * @param codigoBarras el código de barras a buscar
     * @return el producto si se encuentra, vacío en caso contrario
     */
    Optional<Producto> obtenerProductoPorCodigoBarras(String codigoBarras);
    
    /**
     * Calcula el precio con impuesto para un producto.
     *
     * @param id el id del producto
     * @param tasaImpuesto la tasa de impuesto a aplicar
     * @return el precio con impuesto
     * @throws IllegalArgumentException si el producto no existe o la tasa es inválida
     */
    BigDecimal calcularPrecioConImpuesto(Long id, BigDecimal tasaImpuesto);
}