package org.litethinking.supermercado.application.mapper;

import org.litethinking.supermercado.domain.model.Cliente;
import org.litethinking.supermercado.shareddto.supermercado.ClienteDto;

/**
 * Mapper for converting between Cliente domain model and ClienteDto.
 */
public class ClienteMapper {

    /**
     * Converts a Cliente domain model to a ClienteDto.
     *
     * @param cliente the domain model to convert
     * @return the corresponding DTO
     */
    public static ClienteDto toDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        
        return new ClienteDto(
            cliente.getId(),
            cliente.getNombre(),
            cliente.getEmail(),
            cliente.getTelefono(),
            cliente.getDireccion()
        );
    }

    /**
     * Converts a ClienteDto to a Cliente domain model.
     *
     * @param dto the DTO to convert
     * @return the corresponding domain model
     */
    public static Cliente toDomain(ClienteDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Cliente.builder()
            .id(dto.id())
            .nombre(dto.nombre())
            .email(dto.email())
            .telefono(dto.telefono())
            .direccion(dto.direccion())
            .build();
    }
}