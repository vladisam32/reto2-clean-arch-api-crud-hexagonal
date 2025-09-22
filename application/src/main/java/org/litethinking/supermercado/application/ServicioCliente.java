package org.litethinking.supermercado.application;

import org.litethinking.supermercado.shareddto.supermercado.ClienteDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Cliente operations.
 */
public interface ServicioCliente {

    /**
     * Create a new customer.
     *
     * @param clienteDto the customer to create
     * @return the created customer
     */
    ClienteDto crearCliente(ClienteDto clienteDto);

    /**
     * Update an existing customer.
     *
     * @param id the id of the customer to update
     * @param clienteDto the updated customer data
     * @return the updated customer
     */
    ClienteDto actualizarCliente(Long id, ClienteDto clienteDto);

    /**
     * Get a customer by its id.
     *
     * @param id the id of the customer
     * @return the customer if found, empty otherwise
     */
    Optional<ClienteDto> obtenerClientePorId(Long id);

    /**
     * Get all customers.
     *
     * @return the list of all customers
     */
    List<ClienteDto> obtenerTodosLosClientes();

    /**
     * Delete a customer by its id.
     *
     * @param id the id of the customer to delete
     */
    void eliminarCliente(Long id);

    /**
     * Get a customer by its email.
     *
     * @param email the email to search for
     * @return the customer if found, empty otherwise
     */
    Optional<ClienteDto> obtenerClientePorEmail(String email);

    /**
     * Get customers by name containing the given string.
     *
     * @param nombre the name substring to search for
     * @return the list of customers with matching names
     */
    List<ClienteDto> obtenerClientesPorNombreContaining(String nombre);
}
