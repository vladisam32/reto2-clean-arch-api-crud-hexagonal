package org.litethinking.supermercado.application.mapper;

import org.litethinking.supermercado.domain.model.Cajero;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;

/**
 * Mapper for converting between Cajero domain model and CajeroDto.
 */
public class CajeroMapper {

    /**
     * Converts a Cajero domain model to a CajeroDto.
     *
     * @param cajero the domain model to convert
     * @return the corresponding DTO
     */
    public static CajeroDto toDto(Cajero cajero) {
        if (cajero == null) {
            return null;
        }
        
        return new CajeroDto(
            cajero.getId(),
            cajero.getNombre(),
            cajero.getCodigo(),
            cajero.getTurno()
        );
    }

    /**
     * Converts a CajeroDto to a Cajero domain model.
     *
     * @param dto the DTO to convert
     * @return the corresponding domain model
     */
    public static Cajero toDomain(CajeroDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Cajero.builder()
            .id(dto.id())
            .nombre(dto.nombre())
            .codigo(dto.codigo())
            .turno(dto.turno())
            .build();
    }
}