package org.litethinking.supermercado.domain.ports.output;

import org.litethinking.supermercado.domain.model.Producto;
import org.litethinking.supermercado.domain.model.inventario.Inventario;

import java.util.List;
import java.util.Optional;

/**
 * Puerto secundario (salida) para operaciones de persistencia de Inventario.
 * Define las operaciones que cualquier adaptador de persistencia debe implementar.
 */
public interface RepositorioInventarioPort {
    
    /**
     * Guarda un registro de inventario.
     *
     * @param inventario el inventario a guardar
     * @return el inventario guardado
     */
    Inventario save(Inventario inventario);
    
    /**
     * Busca un registro de inventario por su id.
     *
     * @param id el id del inventario
     * @return el inventario si se encuentra, vacío en caso contrario
     */
    Optional<Inventario> findById(Long id);
    
    /**
     * Busca todos los registros de inventario.
     *
     * @return la lista de registros de inventario
     */
    List<Inventario> findAll();
    
    /**
     * Elimina un registro de inventario por su id.
     *
     * @param id el id del inventario a eliminar
     */
    void deleteById(Long id);
    
    /**
     * Busca un registro de inventario por su producto.
     *
     * @param producto el producto a buscar
     * @return el inventario si se encuentra, vacío en caso contrario
     */
    Optional<Inventario> findByProducto(Producto producto);
    
    /**
     * Busca registros de inventario con bajo stock.
     *
     * @return la lista de registros de inventario con bajo stock
     */
    List<Inventario> findBajoStock();
    
    /**
     * Busca registros de inventario por ubicación.
     *
     * @param ubicacion la ubicación a buscar
     * @return la lista de registros de inventario en la ubicación
     */
    List<Inventario> findByUbicacion(String ubicacion);
}