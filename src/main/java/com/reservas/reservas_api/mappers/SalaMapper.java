package com.reservas.reservas_api.mappers;

import com.reservas.reservas_api.dto.SalaRequestDto;
import com.reservas.reservas_api.dto.SalaResponseDto;
import com.reservas.reservas_api.models.SalaEntity;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SalaMapper {

    
    public SalaResponseDto toResponseDto(SalaEntity entity) {
        if (entity == null) return null;
        
        return SalaResponseDto.builder()
                .idSala(entity.getIdSala())
                .nombre(entity.getNombre())
                .capacidad(entity.getCapacidad())
                .build();
    }

    
    public SalaEntity toEntity(SalaRequestDto dto) {
        if (dto == null) return null;

        return SalaEntity.builder()
                .nombre(dto.getNombre())
                .capacidad(dto.getCapacidad())
                .build();
    }

    public List<SalaResponseDto> toResponseDtoList(List<SalaEntity> entities) {
        if (entities == null) return null;

        return entities.stream()
                .map(this::toResponseDto)
                .toList();
    }

    
    public void updateEntity(SalaEntity entity, SalaRequestDto dto) {
        if (dto == null || entity == null) return;
        
        entity.setNombre(dto.getNombre());
        entity.setCapacidad(dto.getCapacidad());
    }
}