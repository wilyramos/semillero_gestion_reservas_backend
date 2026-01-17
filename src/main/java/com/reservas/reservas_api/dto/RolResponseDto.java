package com.reservas.reservas_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolResponseDto {
    private Long idRol;
    private String nombreRol;
}