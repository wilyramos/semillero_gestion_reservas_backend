package com.reservas.reservas_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CancelarReservaRequestDto {
    private Long idReserva;
    private String motivo;
}
