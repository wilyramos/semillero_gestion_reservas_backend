package com.reservas.reservas_api.service;

import com.reservas.reservas_api.dto.CancelarReservaRequestDto;
import com.reservas.reservas_api.dto.CrearReservaRequestDto;

public interface IReservaService {
    void crearReserva(CrearReservaRequestDto request);

    void cancelarReserva(CancelarReservaRequestDto request);
}
