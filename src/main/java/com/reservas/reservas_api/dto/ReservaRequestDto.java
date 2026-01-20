package com.reservas.reservas_api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder 
@AllArgsConstructor
@NoArgsConstructor
public class ReservaRequestDto {
    private Long idSala;
    private Long idUsuario;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
