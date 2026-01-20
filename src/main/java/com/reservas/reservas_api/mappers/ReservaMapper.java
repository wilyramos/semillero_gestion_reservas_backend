package com.reservas.reservas_api.mappers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.reservas.reservas_api.dto.ReservaResponseDto;
import com.reservas.reservas_api.models.ReservaEntity;

@Component
public class ReservaMapper {

    public ReservaResponseDto toResponseDto(ReservaEntity entity) {
        if (entity == null) return null;
        return ReservaResponseDto.builder()
                .idReserva(entity.getIdReserva())
                .nombreSala(entity.getSala().getNombre())
                .username(entity.getUsuario().getUsername())
                .fechaInicio(entity.getFechaInicio())
                .fechaFin(entity.getFechaFin())
                .estado(entity.getEstado())
                .build();
    }

    public List<ReservaResponseDto> toResponseDtoList(List<ReservaEntity> entities) {
        return entities.stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    // 
    public ReservaEntity toEntity(ReservaResponseDto dto) {
        if (dto == null) return null;
        return ReservaEntity.builder()
                .idReserva(dto.getIdReserva())
                .estado(dto.getEstado())
                .fechaInicio(dto.getFechaInicio())
                .fechaFin(dto.getFechaFin())
                .build();
    }
}