package com.reservas.reservas_api.controller;

import com.reservas.reservas_api.dto.UsuarioRequestDto;
import com.reservas.reservas_api.dto.UsuarioResponseDto;
import com.reservas.reservas_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUserService userService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@RequestBody UsuarioRequestDto dto) {
        return new ResponseEntity<>(userService.saveRequest(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> update(@PathVariable Long id, @RequestBody UsuarioRequestDto dto) {
        return ResponseEntity.ok(userService.updateRequest(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> delete(@PathVariable Long id) {
        return ResponseEntity.ok(userService.delete(id));
    }
}