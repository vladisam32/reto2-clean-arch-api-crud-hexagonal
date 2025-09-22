package org.litethinking.supermercado.infrastructure.persistence.adapter;

import org.litethinking.supermercado.domain.model.Cajero;
import org.litethinking.supermercado.domain.ports.output.RepositorioCajeroPort;
import org.litethinking.supermercado.infrastructure.entity.EntidadJpaCajero;
import org.litethinking.supermercado.infrastructure.persistence.repository.RepositorioJpaCajero;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador secundario (driven) que implementa el puerto de repositorio de Cajero.
 * Este adaptador convierte entre el modelo de dominio (Cajero) y la entidad JPA (EntidadJpaCajero)
 * y delega las operaciones de persistencia al repositorio JPA.
 */
@Service
public class AdaptadorRepositorioCajero implements RepositorioCajeroPort {

    private final RepositorioJpaCajero repositorioJpaCajero;

    public AdaptadorRepositorioCajero(RepositorioJpaCajero repositorioJpaCajero) {
        this.repositorioJpaCajero = repositorioJpaCajero;
    }

    /**
     * Guarda un cajero en la base de datos, sea nuevo o actualizao.
     *
     * @param cajero el cajero que vamo' a guardar
     * @return el cajero ya guardao con su ID si e' nuevo
     */
    @Override
    public Cajero save(Cajero cajero) {
        EntidadJpaCajero entidadJpaCajero = mapToEntity(cajero);
        EntidadJpaCajero savedEntity = repositorioJpaCajero.save(entidadJpaCajero);
        return mapToDomain(savedEntity);
    }

    /**
     * Busca un cajero por su ID, a ver si lo encontramo'.
     *
     * @param id el ID del cajero que tamo' buscando
     * @return el cajero si aparece, si no un Optional vacío
     */
    @Override
    public Optional<Cajero> findById(Long id) {
        return repositorioJpaCajero.findById(id).map(this::mapToDomain);
    }

    /**
     * Trae toos los cajeros que hay en el sistema.
     *
     * @return la lista completa de cajeros
     */
    @Override
    public List<Cajero> findAll() {
        return repositorioJpaCajero.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    /**
     * Elimina un cajero del sistema, pa' cuando ya no trabaje aquí.
     *
     * @param id el ID del cajero que vamo' a eliminar
     */
    @Override
    public void deleteById(Long id) {
        repositorioJpaCajero.deleteById(id);
    }

    /**
     * Busca un cajero por su código, ¡al toque!
     *
     * @param codigo el código que tamo' buscando
     * @return el cajero si lo encontramo', si no un Optional vacío
     */
    @Override
    public Optional<Cajero> findByCodigo(String codigo) {
        return repositorioJpaCajero.findByCodigo(codigo).map(this::mapToDomain);
    }

    /**
     * Busca toos los cajeros de un turno específico.
     *
     * @param turno el turno que queremo filtrar
     * @return la lista de cajeros de ese turno
     */
    @Override
    public List<Cajero> findByTurno(String turno) {
        return repositorioJpaCajero.findByTurno(turno).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad JPA a un modelo de dominio.
     * Esto e' pa' que el dominio no sepa na' de JPA.
     *
     * @param entidadJpaCajero la entidad JPA que vamo' a convertir
     * @return el modelo de dominio ya convertío
     */
    private Cajero mapToDomain(EntidadJpaCajero entidadJpaCajero) {
        return Cajero.builder()
                .id(entidadJpaCajero.getId())
                .nombre(entidadJpaCajero.getNombre())
                .codigo(entidadJpaCajero.getCodigo())
                .turno(entidadJpaCajero.getTurno())
                .build();
    }

    /**
     * Convierte un modelo de dominio a una entidad JPA.
     * Esto e' pa' que podamo' guardar el modelo en la base de datos.
     *
     * @param cajero el modelo de dominio que vamo' a convertir
     * @return la entidad JPA ya convertía
     */
    private EntidadJpaCajero mapToEntity(Cajero cajero) {
        return EntidadJpaCajero.builder()
                .id(cajero.getId())
                .nombre(cajero.getNombre())
                .codigo(cajero.getCodigo())
                .turno(cajero.getTurno())
                .build();
    }
}
