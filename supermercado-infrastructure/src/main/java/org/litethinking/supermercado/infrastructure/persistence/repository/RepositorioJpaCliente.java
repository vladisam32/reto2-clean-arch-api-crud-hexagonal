package org.litethinking.supermercado.infrastructure.persistence.repository;

import org.litethinking.supermercado.infrastructure.entity.EntidadJpaCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for EntidadJpaCliente.
 */
@Repository
public interface RepositorioJpaCliente extends JpaRepository<EntidadJpaCliente, Long> {

    /**
     * Find a customer by its email.
     *
     * @param email the email to search for
     * @return the customer if found, empty otherwise
     */
    Optional<EntidadJpaCliente> findByEmail(String email);

    /**
     * Find customers by name containing the given string.
     *
     * @param nombre the name substring to search for
     * @return the list of customers with matching names
     */
    List<EntidadJpaCliente> findByNombreContaining(String nombre);
}
