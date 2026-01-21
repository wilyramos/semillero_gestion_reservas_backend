package com.reservas.reservas_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.reservas.reservas_api.dto.CancelarReservaRequestDto;
import com.reservas.reservas_api.dto.CrearReservaRequestDto;
import com.reservas.reservas_api.dto.DashboardStatsDto;
import com.reservas.reservas_api.dto.ReservaResponseDto;
import com.reservas.reservas_api.exception.BadRequestException;
import com.reservas.reservas_api.service.IReservaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    public static final String DATE_PATTERN = "dd-MM-yyyy HH:mm:ss";
    @Autowired
    private IReservaService reservaService;

    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservaResponseDto>> listarTodas() {
        return ResponseEntity.ok(reservaService.findAll());
    }

    @GetMapping("/calendario")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ReservaResponseDto>> obtenerCalendario() {
        return ResponseEntity.ok(reservaService.getDatosCalendario());
    }

    @GetMapping("/usuario/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ReservaResponseDto>> listarPorUsuario(@PathVariable String username) {
        return ResponseEntity.ok(reservaService.findByUsername(username));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ReservaResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.findById(id));
    }

    @PostMapping("/crear")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ReservaResponseDto> crear(@Valid @RequestBody CrearReservaRequestDto request) {
        ReservaResponseDto response = reservaService.save(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> cancelar(@Valid @RequestBody CancelarReservaRequestDto request) {
        reservaService.cancelarReserva(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ReservaResponseDto>> buscarPorFechas(
            @RequestParam @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime fin) {

        // Validación de lógica básica antes de ir al service
        if (fin.isBefore(inicio)) {
            throw new BadRequestException("La fecha de fin no puede ser anterior a la de inicio");
        }

        return ResponseEntity.ok(reservaService.findByFechaRange(inicio, fin));
    }
}