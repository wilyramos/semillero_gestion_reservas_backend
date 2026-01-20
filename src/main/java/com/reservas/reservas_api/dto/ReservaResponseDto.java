package com.reservas.reservas_api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponseDto {
    private Long idReserva;
    private String nombreSala;
    private String username;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estado;
}
