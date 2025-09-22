package org.litethinking.supermercado.application;

import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Cajero operations.
 */
public interface ServicioCajero {

    /**
     * Create a new cashier.
     *
     * @param cajeroDto the cashier to create
     * @return the created cashier
     */
    CajeroDto crearCajero(CajeroDto cajeroDto);

    /**
     * Update an existing cashier.
     *
     * @param id the id of the cashier to update
     * @param cajeroDto the updated cashier data
     * @return the updated cashier
     */
    CajeroDto actualizarCajero(Long id, CajeroDto cajeroDto);

    /**
     * Get a cashier by its id.
     *
     * @param id the id of the cashier
     * @return the cashier if found, empty otherwise
     */
    Optional<CajeroDto> obtenerCajeroPorId(Long id);

    /**
     * Get all cashiers.
     *
     * @return the list of all cashiers
     */
    List<CajeroDto> obtenerTodosLosCajeros();

    /**
     * Delete a cashier by its id.
     *
     * @param id the id of the cashier to delete
     */
    void eliminarCajero(Long id);

    /**
     * Get a cashier by its code.
     *
     * @param codigo the code to search for
     * @return the cashier if found, empty otherwise
     */
    Optional<CajeroDto> obtenerCajeroPorCodigo(String codigo);

    /**
     * Get cashiers by shift.
     *
     * @param turno the shift to search for
     * @return the list of cashiers in the shift
     */
    List<CajeroDto> obtenerCajerosPorTurno(String turno);
}
