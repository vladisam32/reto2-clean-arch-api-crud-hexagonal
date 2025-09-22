package org.litethinking.supermercado.domain.ports.input;

import org.litethinking.supermercado.domain.model.Cajero;

import java.util.List;
import java.util.Optional;

/**
 * Puerto primario (driving) pa' el servicio de Cajero, maneja toa' la lógica de negocio.
 * Este puerto define cómo las aplicaciones externas se comunican con el dominio pa' realizar operaciones con cajeros.
 */
public interface ServicioCajeroPort {

    /**
     * Crea un cajero nuevo, pa' que atienda la caja.
     *
     * @param cajero el cajero que vamo' a crear
     * @return el cajero ya creao'
     */
    Cajero crearCajero(Cajero cajero);

    /**
     * Actualiza un cajero que ya existe, pa' cambiarle los datos.
     *
     * @param id el ID del cajero que vamo' a actualizar
     * @param cajero los datos nuevos del cajero
     * @return el cajero ya actualizao
     */
    Cajero actualizarCajero(Long id, Cajero cajero);

    /**
     * Busca un cajero por su ID, a ver si lo encontramo'.
     *
     * @param id el ID del cajero que tamo' buscando
     * @return el cajero si aparece, si no un Optional vacío
     */
    Optional<Cajero> obtenerCajeroPorId(Long id);

    /**
     * Trae toos los cajeros que hay en el sistema.
     *
     * @return la lista completa de cajeros
     */
    List<Cajero> obtenerTodosLosCajeros();

    /**
     * Elimina un cajero del sistema, pa' cuando ya no trabaje aquí.
     *
     * @param id el ID del cajero que vamo' a eliminar
     */
    void eliminarCajero(Long id);

    /**
     * Busca un cajero por su código, ¡al toque!
     *
     * @param codigo el código que tamo' buscando
     * @return el cajero si lo encontramo', si no un Optional vacío
     */
    Optional<Cajero> obtenerCajeroPorCodigo(String codigo);

    /**
     * Busca toos los cajeros de un turno específico.
     *
     * @param turno el turno que queremo filtrar
     * @return la lista de cajeros de ese turno
     */
    List<Cajero> obtenerCajerosPorTurno(String turno);
}