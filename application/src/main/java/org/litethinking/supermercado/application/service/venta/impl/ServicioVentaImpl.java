package org.litethinking.supermercado.application.service.venta.impl;

import org.litethinking.supermercado.application.mapper.VentaMapper;
import org.litethinking.supermercado.application.service.venta.ServicioVenta;
import org.litethinking.supermercado.domain.model.venta.Venta;
import org.litethinking.supermercado.domain.ports.output.RepositorioVentaPort;

import org.litethinking.supermercado.shareddto.supermercado.venta.VentaDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the ServicioVenta interface.
 */
@Service
public class ServicioVentaImpl implements ServicioVenta {

    private final RepositorioVentaPort repositorioVentaPort;

    public ServicioVentaImpl(RepositorioVentaPort repositorioVentaPort) {
        this.repositorioVentaPort = repositorioVentaPort;
    }

    @Override
    public VentaDto crearVenta(VentaDto ventaDto) {
        Venta venta = VentaMapper.toDomain(ventaDto);
        Venta ventaCreada = repositorioVentaPort.save(venta);
        return VentaMapper.toDto(ventaCreada);
    }

    @Override
    public VentaDto actualizarVenta(Long id, VentaDto ventaDto) {
        Venta venta = VentaMapper.toDomain(ventaDto);
        venta.setId(id);
        Venta ventaActualizada = repositorioVentaPort.save(venta);
        return VentaMapper.toDto(ventaActualizada);
    }

    @Override
    public Optional<VentaDto> obtenerVentaPorId(Long id) {
        return repositorioVentaPort.findById(id)
                .map(VentaMapper::toDto);
    }

    @Override
    public List<VentaDto> obtenerTodasLasVentas() {
        return repositorioVentaPort.findAll()
                .stream()
                .map(VentaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarVenta(Long id) {
        repositorioVentaPort.deleteById(id);
    }

    @Override
    public List<VentaDto> obtenerVentasPorNombreCliente(String nombreCliente) {
        return repositorioVentaPort.findByNombreCliente(nombreCliente)
                .stream()
                .map(VentaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VentaDto> obtenerVentasEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return repositorioVentaPort.findByFechaVentaBetween(fechaInicio, fechaFin)
                .stream()
                .map(VentaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VentaDto> obtenerVentasPorMontoTotalMayorQue(BigDecimal montoMinimo) {
        return repositorioVentaPort.findByMontoTotalGreaterThan(montoMinimo)
                .stream()
                .map(VentaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VentaDto> obtenerVentasPorMetodoPago(String metodoPago) {
        return repositorioVentaPort.findByMetodoPago(metodoPago)
                .stream()
                .map(VentaMapper::toDto)
                .collect(Collectors.toList());
    }
}
