package com.reservas.reservas_api.controller;

import com.reservas.reservas_api.dto.SalaRequestDto;
import com.reservas.reservas_api.dto.SalaResponseDto;
import com.reservas.reservas_api.service.ISalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas")
@RequiredArgsConstructor
public class SalaController {
    private final ISalaService salaService;

    // obtener todas las salas
    @GetMapping
    public ResponseEntity<List<SalaResponseDto>> getAll() {
        return ResponseEntity.ok(salaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(salaService.findById(id));
    }

    // crear nueva sala solo admin
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalaResponseDto> create(@Valid @RequestBody SalaRequestDto dto) {
        return new ResponseEntity<>(salaService.save(dto), HttpStatus.CREATED);
    }

    // actualizar sala solo admin
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalaResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody SalaRequestDto dto) {
        return ResponseEntity.ok(salaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalaResponseDto> delete(@PathVariable Long id) {
        return ResponseEntity.ok(salaService.delete(id));
    }
}