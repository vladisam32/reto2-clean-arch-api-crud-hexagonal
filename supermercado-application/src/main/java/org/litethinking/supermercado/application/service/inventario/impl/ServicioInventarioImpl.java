package org.litethinking.supermercado.application.service.inventario.impl;

import org.litethinking.supermercado.application.service.inventario.ServicioInventario;
import org.litethinking.supermercado.domain.model.Producto;
import org.litethinking.supermercado.domain.model.inventario.Inventario;
import org.litethinking.supermercado.domain.ports.output.RepositorioInventarioPort;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ServicioInventario.
 */
@Service
public class ServicioInventarioImpl implements ServicioInventario {

    private final RepositorioInventarioPort repositorioInventarioPort;

    public ServicioInventarioImpl(RepositorioInventarioPort repositorioInventarioPort) {
        this.repositorioInventarioPort = repositorioInventarioPort;
    }

    @Override
    public Inventario crearInventario(Inventario inventario) {
        return repositorioInventarioPort.save(inventario);
    }

    @Override
    public Inventario actualizarInventario(Long id, Inventario inventario) {
        Optional<Inventario> inventarioExistente = repositorioInventarioPort.findById(id);
        if (inventarioExistente.isPresent()) {
            inventario.setId(id);
            return repositorioInventarioPort.save(inventario);
        } else {
            throw new IllegalArgumentException("Inventario no encontrado con ID: " + id);
        }
    }

    @Override
    public Optional<Inventario> obtenerInventarioPorId(Long id) {
        return repositorioInventarioPort.findById(id);
    }

    @Override
    public List<Inventario> obtenerTodoElInventario() {
        return repositorioInventarioPort.findAll();
    }

    @Override
    public void eliminarInventario(Long id) {
        repositorioInventarioPort.deleteById(id);
    }

    @Override
    public Optional<Inventario> obtenerInventarioPorProducto(Producto producto) {
        return repositorioInventarioPort.findByProducto(producto);
    }

    @Override
    public List<Inventario> obtenerInventarioConBajoStock() {
        return repositorioInventarioPort.findBajoStock();
    }

    @Override
    public List<Inventario> obtenerInventarioPorUbicacion(String ubicacion) {
        return repositorioInventarioPort.findByUbicacion(ubicacion);
    }

    @Override
    public Inventario actualizarCantidadInventario(Long id, Integer cantidad) {
        Optional<Inventario> inventarioExistente = repositorioInventarioPort.findById(id);
        if (inventarioExistente.isPresent()) {
            Inventario inventario = inventarioExistente.get();
            inventario.setCantidad(cantidad);
            return repositorioInventarioPort.save(inventario);
        } else {
            throw new IllegalArgumentException("Inventario no encontrado con ID: " + id);
        }
    }
}
