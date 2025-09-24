package org.litethinking.supermercado.domain.ports.output;

import org.litethinking.supermercado.domain.model.Cajero;

import java.util.List;
import java.util.Optional;

/**
 * Puerto secundario (driven) para el repositorio de Cajero, maneja toda la persistencia.
 * Este puerto define cómo el dominio se comunica con la capa de infraestructura para guardar y buscar cajeros.
 */
public interface RepositorioCajeroPort {

    /**
     * Guarda un cajero en la base de datos, sea nuevo o actualizado.
     *
     * @param cajero el cajero que vamos a guardar
     * @return el cajero ya guardado con su ID si es nuevo
     */
    Cajero save(Cajero cajero);

    /**
     * Busca un cajero por su ID.
     *
     * @param id el ID del cajero que estamos buscando
     * @return el cajero si aparece, si no un Optional vacío
     */
    Optional<Cajero> findById(Long id);

    /**
     * Trae todos los cajeros que hay en el sistema.
     *
     * @return la lista completa de cajeros
     */
    List<Cajero> findAll();

    /**
     * Elimina un cajero del sistema.
     *
     * @param id el ID del cajero que vamos a eliminar
     */
    void deleteById(Long id);

    /**
     * Busca un cajero por su código.
     *
     * @param codigo el código que estamos buscando
     * @return el cajero si lo encontramos, si no un Optional vacío
     */
    Optional<Cajero> findByCodigo(String codigo);

    /**
     * Busca todos los cajeros de un turno específico.
     *
     * @param turno el turno que queremos filtrar
     * @return la lista de cajeros de ese turno
     */
    List<Cajero> findByTurno(String turno);
}
