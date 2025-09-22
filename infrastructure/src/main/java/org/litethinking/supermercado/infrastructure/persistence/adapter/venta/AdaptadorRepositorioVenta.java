package org.litethinking.supermercado.infrastructure.persistence.adapter.venta;

import org.litethinking.supermercado.domain.model.Producto;
import org.litethinking.supermercado.domain.model.venta.ItemVenta;
import org.litethinking.supermercado.domain.model.venta.Venta;
import org.litethinking.supermercado.domain.ports.output.RepositorioVentaPort;
import org.litethinking.supermercado.domain.repository.venta.RepositorioVenta;
import org.litethinking.supermercado.infrastructure.entity.EntidadJpaProducto;
import org.litethinking.supermercado.infrastructure.entity.venta.EntidadJpaItemVenta;
import org.litethinking.supermercado.infrastructure.entity.venta.EntidadJpaVenta;
import org.litethinking.supermercado.infrastructure.persistence.repository.venta.RepositorioJpaVenta;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter implementation for RepositorioVenta and RepositorioVentaPort.
 * This adapter connects the domain layer with the persistence infrastructure.
 */
@Service
public class AdaptadorRepositorioVenta implements RepositorioVentaPort {

    private final RepositorioJpaVenta repositorioJpaVenta;

    public AdaptadorRepositorioVenta(RepositorioJpaVenta repositorioJpaVenta) {
        this.repositorioJpaVenta = repositorioJpaVenta;
    }

    @Override
    public Venta save(Venta venta) {
        EntidadJpaVenta entidadJpaVenta = mapToEntity(venta);
        EntidadJpaVenta savedEntity = repositorioJpaVenta.save(entidadJpaVenta);
        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<Venta> findById(Long id) {
        return repositorioJpaVenta.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Venta> findAll() {
        return repositorioJpaVenta.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repositorioJpaVenta.deleteById(id);
    }

    @Override
    public List<Venta> findByFechaVentaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return repositorioJpaVenta.findByFechaVentaBetween(fechaInicio, fechaFin).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Venta> findByNombreCliente(String nombreCliente) {
        return repositorioJpaVenta.findByNombreCliente(nombreCliente).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Venta> findByMontoTotalGreaterThan(BigDecimal montoMinimo) {
        return repositorioJpaVenta.findByMontoTotalGreaterThan(montoMinimo).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Venta> findByMetodoPago(String metodoPago) {
        return repositorioJpaVenta.findByMetodoPago(metodoPago).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    private Venta mapToDomain(EntidadJpaVenta entidadJpaVenta) {
        List<ItemVenta> items = entidadJpaVenta.getItems().stream()
                .map(this::mapItemToDomain)
                .collect(Collectors.toList());

        return Venta.builder()
                .id(entidadJpaVenta.getId())
                .fechaVenta(entidadJpaVenta.getFechaVenta())
                .nombreCliente(entidadJpaVenta.getNombreCliente())
                .items(items)
                .montoTotal(entidadJpaVenta.getMontoTotal())
                .metodoPago(entidadJpaVenta.getMetodoPago())
                .build();
    }

    private EntidadJpaVenta mapToEntity(Venta venta) {
        EntidadJpaVenta entidadJpaVenta = EntidadJpaVenta.builder()
                .id(venta.getId())
                .fechaVenta(venta.getFechaVenta())
                .nombreCliente(venta.getNombreCliente())
                .montoTotal(venta.getMontoTotal())
                .metodoPago(venta.getMetodoPago())
                .build();

        if (venta.getItems() != null) {
            venta.getItems().forEach(item -> {
                EntidadJpaItemVenta entidadJpaItemVenta = mapItemToEntity(item);
                entidadJpaVenta.addItem(entidadJpaItemVenta);
            });
        }

        return entidadJpaVenta;
    }

    private ItemVenta mapItemToDomain(EntidadJpaItemVenta entidadJpaItemVenta) {
        return ItemVenta.builder()
                .id(entidadJpaItemVenta.getId())
                .producto(mapProductoToDomain(entidadJpaItemVenta.getProducto()))
                .cantidad(entidadJpaItemVenta.getCantidad())
                .precioUnitario(entidadJpaItemVenta.getPrecioUnitario())
                .subtotal(entidadJpaItemVenta.getSubtotal())
                .build();
    }

    private EntidadJpaItemVenta mapItemToEntity(ItemVenta itemVenta) {
        return EntidadJpaItemVenta.builder()
                .id(itemVenta.getId())
                .producto(mapProductoToEntity(itemVenta.getProducto()))
                .cantidad(itemVenta.getCantidad())
                .precioUnitario(itemVenta.getPrecioUnitario())
                .subtotal(itemVenta.getSubtotal())
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
