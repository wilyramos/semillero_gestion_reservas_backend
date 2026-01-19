package com.reservas.reservas_api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrearReservaRequestDto {

    private Long idSala;
    private Long idUsuario;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
