package org.litethinking.supermercado.infrastructure.persistence.adapter.inventario;

import org.litethinking.supermercado.domain.model.Producto;
import org.litethinking.supermercado.domain.model.inventario.Inventario;
import org.litethinking.supermercado.domain.ports.output.RepositorioInventarioPort;
import org.litethinking.supermercado.infrastructure.entity.EntidadJpaProducto;
import org.litethinking.supermercado.infrastructure.entity.inventario.EntidadJpaInventario;
import org.litethinking.supermercado.infrastructure.persistence.repository.inventario.RepositorioJpaInventario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter implementation for RepositorioInventario and RepositorioInventarioPort.
 * This adapter connects the domain layer with the persistence infrastructure.
 */
@Service
public class AdaptadorRepositorioInventario implements RepositorioInventarioPort {

    private final RepositorioJpaInventario repositorioJpaInventario;

    public AdaptadorRepositorioInventario(RepositorioJpaInventario repositorioJpaInventario) {
        this.repositorioJpaInventario = repositorioJpaInventario;
    }

    @Override
    public Inventario save(Inventario inventario) {
        EntidadJpaInventario entidadJpaInventario = mapToEntity(inventario);
        EntidadJpaInventario savedEntity = repositorioJpaInventario.save(entidadJpaInventario);
        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<Inventario> findById(Long id) {
        return repositorioJpaInventario.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Inventario> findAll() {
        return repositorioJpaInventario.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repositorioJpaInventario.deleteById(id);
    }

    @Override
    public Optional<Inventario> findByProducto(Producto producto) {
        EntidadJpaProducto entidadJpaProducto = mapProductoToEntity(producto);
        return repositorioJpaInventario.findByProducto(entidadJpaProducto).map(this::mapToDomain);
    }

    @Override
    public List<Inventario> findBajoStock() {
        return repositorioJpaInventario.findBajoStock().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Inventario> findByUbicacion(String ubicacion) {
        return repositorioJpaInventario.findByUbicacion(ubicacion).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    private Inventario mapToDomain(EntidadJpaInventario entidadJpaInventario) {
        return Inventario.builder()
                .id(entidadJpaInventario.getId())
                .producto(mapProductoToDomain(entidadJpaInventario.getProducto()))
                .cantidad(entidadJpaInventario.getCantidad())
                .stockMinimo(entidadJpaInventario.getStockMinimo())
                .stockMaximo(entidadJpaInventario.getStockMaximo())
                .fechaUltimaReposicion(entidadJpaInventario.getFechaUltimaReposicion())
                .ubicacion(entidadJpaInventario.getUbicacion())
                .build();
    }

    private EntidadJpaInventario mapToEntity(Inventario inventario) {
        return EntidadJpaInventario.builder()
                .id(inventario.getId())
                .producto(mapProductoToEntity(inventario.getProducto()))
                .cantidad(inventario.getCantidad())
                .stockMinimo(inventario.getStockMinimo())
                .stockMaximo(inventario.getStockMaximo())
                .fechaUltimaReposicion(inventario.getFechaUltimaReposicion())
                .ubicacion(inventario.getUbicacion())
                .build();
    }

    private Producto mapProductoToDomain(EntidadJpaProducto entidadJpaProducto) {
        return Producto.builder()
                .id(entidadJpaProducto.getId())
                .nombre(entidadJpaProducto.getNombre())
                .descripcion(entidadJpaProducto.getDescripcion())
                .precio(entidadJpaProducto.getPrecio())
                .categoria(entidadJpaProducto.getCategoria())
                .codigoBarras(entidadJpaProducto.getCodigoBarras())
                .build();
    }

    private EntidadJpaProducto mapProductoToEntity(Producto producto) {
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
