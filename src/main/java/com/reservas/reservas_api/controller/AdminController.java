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
import com.reservas.reservas_api.service.IReservaService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private IReservaService reservaService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats() {
        return ResponseEntity.ok(reservaService.getAdminStats());
    }

    @PostMapping("/reservas/paginado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageImpl<ReservaResponseDto>> getReservasPaginado(
            @RequestBody PaginationModel paginationModel) {
        return ResponseEntity.ok(reservaService.getPagination(paginationModel));
    }
}