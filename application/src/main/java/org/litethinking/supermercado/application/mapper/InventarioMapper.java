package org.litethinking.supermercado.application.mapper;

import org.litethinking.supermercado.domain.model.inventario.Inventario;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;

/**
 * Mapper for converting between Inventario domain model and InventarioDto.
 */
public class InventarioMapper {

    /**
     * Converts an Inventario domain model to an InventarioDto.
     *
     * @param inventario the domain model to convert
     * @return the corresponding DTO
     */
    public static InventarioDto toDto(Inventario inventario) {
        if (inventario == null) {
            return null;
        }
        
        return new InventarioDto(
            inventario.getId(),
            ProductoMapper.toDto(inventario.getProducto()),
            inventario.getCantidad(),
            inventario.getStockMinimo(),
            inventario.getStockMaximo(),
            inventario.getFechaUltimaReposicion(),
            inventario.getUbicacion()
        );
    }

    /**
     * Converts an InventarioDto to an Inventario domain model.
     *
     * @param dto the DTO to convert
     * @return the corresponding domain model
     */
    public static Inventario toDomain(InventarioDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Inventario.builder()
            .id(dto.id())
            .producto(ProductoMapper.toDomain(dto.producto()))
            .cantidad(dto.cantidad())
            .stockMinimo(dto.stockMinimo())
            .stockMaximo(dto.stockMaximo())
            .fechaUltimaReposicion(dto.fechaUltimaReposicion())
            .ubicacion(dto.ubicacion())
            .build();
    }
}