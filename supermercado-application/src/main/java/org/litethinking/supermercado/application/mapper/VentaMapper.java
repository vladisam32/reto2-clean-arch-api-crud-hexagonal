package org.litethinking.supermercado.application.mapper;

import org.litethinking.supermercado.domain.model.venta.ItemVenta;
import org.litethinking.supermercado.domain.model.venta.Venta;
import org.litethinking.supermercado.shareddto.supermercado.venta.ItemVentaDto;
import org.litethinking.supermercado.shareddto.supermercado.venta.VentaDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Venta domain model and VentaDto.
 */
public class VentaMapper {

    /**
     * Converts a Venta domain model to a VentaDto.
     *
     * @param venta the domain model to convert
     * @return the corresponding DTO
     */
    public static VentaDto toDto(Venta venta) {
        if (venta == null) {
            return null;
        }
        
        List<ItemVentaDto> itemsDto = null;
        if (venta.getItems() != null) {
            itemsDto = venta.getItems().stream()
                .map(VentaMapper::toItemDto)
                .collect(Collectors.toList());
        }
        
        return new VentaDto(
            venta.getId(),
            venta.getFechaVenta(),
            venta.getNombreCliente(),
            itemsDto,
            venta.getMontoTotal(),
            venta.getMetodoPago()
        );
    }

    /**
     * Converts a VentaDto to a Venta domain model.
     *
     * @param dto the DTO to convert
     * @return the corresponding domain model
     */
    public static Venta toDomain(VentaDto dto) {
        if (dto == null) {
            return null;
        }
        
        List<ItemVenta> items = null;
        if (dto.items() != null) {
            items = dto.items().stream()
                .map(VentaMapper::toItemDomain)
                .collect(Collectors.toList());
        }
        
        return Venta.builder()
            .id(dto.id())
            .fechaVenta(dto.fechaVenta())
            .nombreCliente(dto.nombreCliente())
            .items(items)
            .montoTotal(dto.montoTotal())
            .metodoPago(dto.metodoPago())
            .build();
    }
    
    /**
     * Converts an ItemVenta domain model to an ItemVentaDto.
     *
     * @param itemVenta the domain model to convert
     * @return the corresponding DTO
     */
    public static ItemVentaDto toItemDto(ItemVenta itemVenta) {
        if (itemVenta == null) {
            return null;
        }
        
        return new ItemVentaDto(
            itemVenta.getId(),
            ProductoMapper.toDto(itemVenta.getProducto()),
            itemVenta.getCantidad(),
            itemVenta.getPrecioUnitario(),
            itemVenta.getSubtotal()
        );
    }
    
    /**
     * Converts an ItemVentaDto to an ItemVenta domain model.
     *
     * @param dto the DTO to convert
     * @return the corresponding domain model
     */
    public static ItemVenta toItemDomain(ItemVentaDto dto) {
        if (dto == null) {
            return null;
        }
        
        return ItemVenta.builder()
            .id(dto.id())
            .producto(ProductoMapper.toDomain(dto.producto()))
            .cantidad(dto.cantidad())
            .precioUnitario(dto.precioUnitario())
            .subtotal(dto.subtotal())
            .build();
    }
}