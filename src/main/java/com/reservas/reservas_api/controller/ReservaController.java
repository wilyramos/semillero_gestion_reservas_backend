package com.reservas.reservas_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.reservas.reservas_api.dto.CancelarReservaRequestDto;
import com.reservas.reservas_api.dto.CancelarReservaResponseDto;
import com.reservas.reservas_api.dto.CrearReservaRequestDto;
import com.reservas.reservas_api.dto.CrearReservaResponseDto;
import com.reservas.reservas_api.service.IReservaService;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private  IReservaService reservaService;

    @PostMapping("/crear")
    public ResponseEntity<CrearReservaResponseDto> crear(
            @Valid @RequestBody CrearReservaRequestDto request) {

        reservaService.crearReserva(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new CrearReservaResponseDto("Reserva creada correctamente"));
    }

    @PostMapping("/cancelar")
    public ResponseEntity<CancelarReservaResponseDto> cancelar(
            @Valid @RequestBody CancelarReservaRequestDto request) {

        reservaService.cancelarReserva(request);
        return ResponseEntity.ok(
            new CancelarReservaResponseDto("Reserva cancelada correctamente")
        );
    }
}

