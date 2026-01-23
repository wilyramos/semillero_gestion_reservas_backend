package com.reservas.reservas_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservas.reservas_api.commons.PaginationModel;
import com.reservas.reservas_api.dto.DashboardStatsDto;
import com.reservas.reservas_api.dto.ReservaResponseDto;
import com.reservas.reservas_api.dto.UsuarioResponseDto;
import com.reservas.reservas_api.service.IReservaService;
import com.reservas.reservas_api.service.IUserService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IReservaService reservaService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats() {
        return ResponseEntity.ok(reservaService.getAdminStats());
    }

    @PostMapping("/reservas/paginado")
    public ResponseEntity<PageImpl<ReservaResponseDto>> getReservasPaginado(
            @RequestBody PaginationModel paginationModel) {
        return ResponseEntity.ok(reservaService.getPagination(paginationModel));
    }

    @PostMapping("/usuarios/paginado")
    public ResponseEntity<PageImpl<UsuarioResponseDto>> getUsuarios(@RequestBody PaginationModel params) {
        return ResponseEntity.ok(userService.getPagination(params));
    }
}