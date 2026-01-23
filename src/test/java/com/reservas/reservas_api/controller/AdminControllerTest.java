package com.reservas.reservas_api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.reservas.reservas_api.commons.PaginationModel;
import com.reservas.reservas_api.dto.DashboardStatsDto;
import com.reservas.reservas_api.dto.ReservaResponseDto;
import com.reservas.reservas_api.dto.UsuarioResponseDto;
import com.reservas.reservas_api.exception.BadRequestException;
import com.reservas.reservas_api.service.IReservaService;
import com.reservas.reservas_api.service.IUserService;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    private final DashboardStatsDto DASHBOARD_STATS_DTO = new DashboardStatsDto();
    private final PaginationModel PAGINATION_MODEL = new PaginationModel();
    private final ReservaResponseDto RESERVA_RESPONSE_DTO = new ReservaResponseDto();
    private final UsuarioResponseDto USUARIO_RESPONSE_DTO = new UsuarioResponseDto();

    @Mock
    private IUserService userService;

    @Mock
    private IReservaService reservaService;

    @InjectMocks
    private AdminController adminController;

    private PageImpl<ReservaResponseDto> reservasPage;
    private PageImpl<UsuarioResponseDto> usuariosPage;

    @BeforeEach
    void setUp() {
        DASHBOARD_STATS_DTO.setTotalReservasHoy(3L);
        DASHBOARD_STATS_DTO.setSalaMayorDemanda("Sala A");
        DASHBOARD_STATS_DTO.setReservasPorSalaSemana(Map.of("Sala A", 2L, "Sala B", 1L));
        DASHBOARD_STATS_DTO.setHorasPico(Map.of(9, 2L, 10, 1L));

        PAGINATION_MODEL.setPageNumber(0);
        PAGINATION_MODEL.setRowsPerPage(10);

        RESERVA_RESPONSE_DTO.setIdReserva(1L);
        RESERVA_RESPONSE_DTO.setNombreSala("Sala A");
        RESERVA_RESPONSE_DTO.setUsername("admin");
        RESERVA_RESPONSE_DTO.setEstado("CONFIRMADA");
        reservasPage = new PageImpl<>(List.of(RESERVA_RESPONSE_DTO));

        USUARIO_RESPONSE_DTO.setIdUsuario(1L);
        USUARIO_RESPONSE_DTO.setUsername("admin");
        USUARIO_RESPONSE_DTO.setRoleNames(List.of("ADMIN"));
        usuariosPage = new PageImpl<>(List.of(USUARIO_RESPONSE_DTO));
    }

    @Test
    void getStats() {
        when(reservaService.getAdminStats()).thenReturn(DASHBOARD_STATS_DTO);

        ResponseEntity<DashboardStatsDto> response = adminController.getStats();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(DASHBOARD_STATS_DTO, response.getBody());
    }

    @Test
    void getReservasPaginado() {
        when(reservaService.getPagination(PAGINATION_MODEL)).thenReturn(reservasPage);

        ResponseEntity<PageImpl<ReservaResponseDto>> response = adminController.getReservasPaginado(PAGINATION_MODEL);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservasPage, response.getBody());
    }

    @Test
    void getUsuarios() {
        when(userService.getPagination(PAGINATION_MODEL)).thenReturn(usuariosPage);

        ResponseEntity<PageImpl<UsuarioResponseDto>> response = adminController.getUsuarios(PAGINATION_MODEL);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuariosPage, response.getBody());
    }

    // Tests de casos de error

    @Test
    void getReservasPaginadoWithInvalidPage() {
        PaginationModel invalidPagination = new PaginationModel();
        invalidPagination.setPageNumber(-1);
        invalidPagination.setRowsPerPage(10);

        when(reservaService.getPagination(invalidPagination))
                .thenThrow(new BadRequestException("El número de página no puede ser negativo"));

        assertThrows(BadRequestException.class, 
                () -> adminController.getReservasPaginado(invalidPagination));
    }

    @Test
    void getReservasPaginadoWithInvalidPageSize() {
        PaginationModel invalidPagination = new PaginationModel();
        invalidPagination.setPageNumber(0);
        invalidPagination.setRowsPerPage(0);

        when(reservaService.getPagination(invalidPagination))
                .thenThrow(new BadRequestException("El tamaño de página debe ser mayor a cero"));

        assertThrows(BadRequestException.class, 
                () -> adminController.getReservasPaginado(invalidPagination));
    }

    @Test
    void getUsuariosWithInvalidPage() {
        PaginationModel invalidPagination = new PaginationModel();
        invalidPagination.setPageNumber(-1);
        invalidPagination.setRowsPerPage(10);

        when(userService.getPagination(invalidPagination))
                .thenThrow(new BadRequestException("El número de página no puede ser negativo"));

        assertThrows(BadRequestException.class, 
                () -> adminController.getUsuarios(invalidPagination));
    }

    @Test
    void getStatsWithNoData() {
        DashboardStatsDto emptyStats = new DashboardStatsDto();
        when(reservaService.getAdminStats()).thenReturn(emptyStats);

        ResponseEntity<DashboardStatsDto> response = adminController.getStats();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyStats, response.getBody());
    }

    @Test
    void getReservasPaginadoWithNullModel() {
        when(reservaService.getPagination(null))
                .thenThrow(new IllegalArgumentException("El modelo de paginación no puede ser nulo"));

        assertThrows(IllegalArgumentException.class, 
                () -> adminController.getReservasPaginado(null));
    }
}
