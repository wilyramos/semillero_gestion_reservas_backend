package com.reservas.reservas_api.mappers;

import com.reservas.reservas_api.dto.UsuarioRequestDto;
import com.reservas.reservas_api.dto.UsuarioResponseDto;
import com.reservas.reservas_api.models.UsuarioEntity;
import com.reservas.reservas_api.models.UsuarioRol;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    public UsuarioResponseDto toResponseDto(UsuarioEntity entity, List<UsuarioRol> roles) {
        return UsuarioResponseDto.builder()
                .idUsuario(entity.getIdUsuario())
                .username(entity.getUsername())
                .roleNames(roles.stream()
                        .map(ur -> ur.getRol().getNombreRol())
                        .collect(Collectors.toList()))
                .build();
    }

    public UsuarioEntity toEntity(UsuarioRequestDto dto) {
        return UsuarioEntity.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }
}