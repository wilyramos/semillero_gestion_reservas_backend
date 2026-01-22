package com.reservas.reservas_api.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.reservas.reservas_api.commons.ICrudCommonsDto;
import com.reservas.reservas_api.commons.PaginationModel;
import com.reservas.reservas_api.dto.CancelarReservaRequestDto;
import com.reservas.reservas_api.dto.CrearReservaRequestDto;
import com.reservas.reservas_api.dto.DashboardStatsDto;
import com.reservas.reservas_api.dto.ReservaResponseDto;


public interface IReservaService extends ICrudCommonsDto<CrearReservaRequestDto, ReservaResponseDto, Long> {
    
    void cancelarReserva(CancelarReservaRequestDto request);
    
    List<ReservaResponseDto> findAll();
    
    List<ReservaResponseDto> findByUsername(String username);

    // para calendario
    List<ReservaResponseDto> getDatosCalendario();

    // filtro por fecha
    List<ReservaResponseDto> findByFechaRange(java.time.LocalDateTime inicio, java.time.LocalDateTime fin);

    // para dashboard admin
    DashboardStatsDto getAdminStats();

    // para reservas paginadas con filtros y orden
    PageImpl<ReservaResponseDto> getPagination(PaginationModel paginationModel);

    // para reservas paginadas por usuario
    PageImpl<ReservaResponseDto> getPaginationByUser(String username, PaginationModel paginationModel);
}