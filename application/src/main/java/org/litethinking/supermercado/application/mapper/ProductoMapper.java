package org.litethinking.supermercado.application.mapper;

import org.litethinking.supermercado.domain.model.Producto;
import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;

/**
 * Mapper for converting between Producto domain model and ProductoDto.
 */
public class ProductoMapper {

    /**
     * Converts a Producto domain model to a ProductoDto.
     *
     * @param producto the domain model to convert
     * @return the corresponding DTO
     */
    public static ProductoDto toDto(Producto producto) {
        if (producto == null) {
            return null;
        }
        
        return new ProductoDto(
            producto.getId(),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getPrecio(),
            producto.getCategoria(),
            producto.getCodigoBarras()
        );
    }

    /**
     * Converts a ProductoDto to a Producto domain model.
     *
     * @param dto the DTO to convert
     * @return the corresponding domain model
     */
    public static Producto toDomain(ProductoDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Producto.builder()
            .id(dto.id())
            .nombre(dto.nombre())
            .descripcion(dto.descripcion())
            .precio(dto.precio())
            .categoria(dto.categoria())
            .codigoBarras(dto.codigoBarras())
            .build();
    }
}