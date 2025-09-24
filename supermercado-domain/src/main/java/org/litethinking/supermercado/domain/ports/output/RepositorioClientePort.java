package org.litethinking.supermercado.domain.ports.output;

import org.litethinking.supermercado.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

/**
 * Puerto secundario (salida) para operaciones de persistencia de Cliente.
 * Define las operaciones que cualquier adaptador de persistencia debe implementar.
 */
public interface RepositorioClientePort {
    
    /**
     * Guarda un cliente.
     *
     * @param cliente el cliente a guardar
     * @return el cliente guardado
     */
    Cliente save(Cliente cliente);
    
    /**
     * Busca un cliente por su id.
     *
     * @param id el id del cliente
     * @return el cliente si se encuentra, vacío en caso contrario
     */
    Optional<Cliente> findById(Long id);
    
    /**
     * Busca todos los clientes.
     *
     * @return la lista de clientes
     */
    List<Cliente> findAll();
    
    /**
     * Elimina un cliente por su id.
     *
     * @param id el id del cliente a eliminar
     */
    void deleteById(Long id);
    
    /**
     * Busca un cliente por su email.
     *
     * @param email el email a buscar
     * @return el cliente si se encuentra, vacío en caso contrario
     */
    Optional<Cliente> findByEmail(String email);
    
    /**
     * Busca clientes por nombre que contenga la cadena dada.
     *
     * @param nombre la subcadena de nombre a buscar
     * @return la lista de clientes con nombres coincidentes
     */
    List<Cliente> findByNombreContaining(String nombre);
}