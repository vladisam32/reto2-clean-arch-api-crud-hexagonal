package org.litethinking.supermercado.infrastructure.persistence.repository;

import org.litethinking.supermercado.infrastructure.entity.EntidadJpaCajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for EntidadJpaCajero.
 */
@Repository
public interface RepositorioJpaCajero extends JpaRepository<EntidadJpaCajero, Long> {

    /**
     * Find a cashier by its code.
     *
     * @param codigo the code to search for
     * @return the cashier if found, empty otherwise
     */
    Optional<EntidadJpaCajero> findByCodigo(String codigo);

    /**
     * Find cashiers by shift.
     *
     * @param turno the shift to search for
     * @return the list of cashiers in the shift
     */
    List<EntidadJpaCajero> findByTurno(String turno);
}
