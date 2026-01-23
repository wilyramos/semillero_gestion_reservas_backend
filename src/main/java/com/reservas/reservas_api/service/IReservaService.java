package com.reservas.reservas_api.service;

import java.time.LocalDateTime;
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

    /**
     * Obtiene datos simplificados para el calendario (Vista general).
     */
    List<ReservaResponseDto> getDatosCalendario();

    /**
     * Búsqueda avanzada por rango de fechas y filtros opcionales.
     * Utilizado principalmente por el componente de calendario con filtros dinámicos.
     */
    List<ReservaResponseDto> findByFechaRange(
            LocalDateTime inicio, 
            LocalDateTime fin, 
            Long idSala, 
            String estado, 
            String username, 
            String nombreSala
    );

    /**
     * Obtiene estadísticas para el dashboard administrativo.
     */
    DashboardStatsDto getAdminStats();

    /**
     * Paginación global para el administrador con soporte de filtros y ordenamiento.
     */
    PageImpl<ReservaResponseDto> getPagination(PaginationModel paginationModel);

    /**
     * Paginación específica para un usuario (Vista "Mis Reservas").
     */
    PageImpl<ReservaResponseDto> getPaginationByUser(String username, PaginationModel paginationModel);
}