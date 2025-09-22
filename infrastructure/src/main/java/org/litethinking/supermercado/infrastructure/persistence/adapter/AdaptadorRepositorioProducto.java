package org.litethinking.supermercado.infrastructure.persistence.adapter;

import org.litethinking.supermercado.domain.model.Producto;
import org.litethinking.supermercado.domain.ports.output.RepositorioProductoPort;
import org.litethinking.supermercado.domain.repository.RepositorioProducto;
import org.litethinking.supermercado.infrastructure.entity.EntidadJpaProducto;
import org.litethinking.supermercado.infrastructure.persistence.repository.RepositorioJpaProducto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter implementation for RepositorioProducto and RepositorioProductoPort.
 * This adapter connects the domain layer with the persistence infrastructure.
 */
@Service
public class AdaptadorRepositorioProducto implements RepositorioProductoPort {

    private final RepositorioJpaProducto repositorioJpaProducto;

    public AdaptadorRepositorioProducto(RepositorioJpaProducto repositorioJpaProducto) {
        this.repositorioJpaProducto = repositorioJpaProducto;
    }

    @Override
    public Producto save(Producto producto) {
        EntidadJpaProducto entidadJpaProducto = mapToEntity(producto);
        EntidadJpaProducto savedEntity = repositorioJpaProducto.save(entidadJpaProducto);
        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return repositorioJpaProducto.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Producto> findAll() {
        return repositorioJpaProducto.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repositorioJpaProducto.deleteById(id);
    }

    @Override
    public List<Producto> findByCategoria(String categoria) {
        return repositorioJpaProducto.findByCategoria(categoria).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> findByPrecioBetween(BigDecimal precioMinimo, BigDecimal precioMaximo) {
        return repositorioJpaProducto.findByPrecioBetween(precioMinimo, precioMaximo).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Producto> findByCodigoBarras(String codigoBarras) {
        return repositorioJpaProducto.findByCodigoBarras(codigoBarras).map(this::mapToDomain);
    }

    private Producto mapToDomain(EntidadJpaProducto entidadJpaProducto) {
        return Producto.builder()
                .id(entidadJpaProducto.getId())
                .nombre(entidadJpaProducto.getNombre())
                .descripcion(entidadJpaProducto.getDescripcion())
                .precio(entidadJpaProducto.getPrecio())
                .categoria(entidadJpaProducto.getCategoria())
                .codigoBarras(entidadJpaProducto.getCodigoBarras())
                .build();
    }

    private EntidadJpaProducto mapToEntity(Producto producto) {
        return EntidadJpaProducto.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .categoria(producto.getCategoria())
                .codigoBarras(producto.getCodigoBarras())
                .build();
    }
}
