package com.reservas.reservas_api.service.impl;

import com.reservas.reservas_api.dto.*;
import com.reservas.reservas_api.exception.*;
import com.reservas.reservas_api.mappers.ReservaMapper;
import com.reservas.reservas_api.models.ReservaEntity;
import com.reservas.reservas_api.repository.IReservaRepository;
import com.reservas.reservas_api.service.IReservaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements IReservaService {

    private final IReservaRepository reservaRepository;
    private final ReservaMapper reservaMapper;
    private final EntityManager entityManager;

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Override
    @Transactional
    public ReservaResponseDto save(CrearReservaRequestDto request) {
        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("RESERVAS.pkg_reservas.pr_crear_reserva");

        // El orden debe ser el mismo que en el Package
        q.registerStoredProcedureParameter("p_id_sala", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_id_usuario", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_fecha_inicio", java.time.LocalDateTime.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_fecha_fin", java.time.LocalDateTime.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_id_reserva", Long.class, ParameterMode.OUT);
        q.registerStoredProcedureParameter("p_codigo_salida", Integer.class, ParameterMode.OUT);
        q.registerStoredProcedureParameter("p_mensaje_salida", String.class, ParameterMode.OUT);

        q.setParameter("p_id_sala", request.getIdSala());
        q.setParameter("p_id_usuario", request.getIdUsuario());
        q.setParameter("p_fecha_inicio", request.getFechaInicio());
        q.setParameter("p_fecha_fin", request.getFechaFin());

        q.execute();

        Integer codigo = (Integer) q.getOutputParameterValue("p_codigo_salida");
        String mensaje = (String) q.getOutputParameterValue("p_mensaje_salida");
        Long idGenerado = (Long) q.getOutputParameterValue("p_id_reserva");

        if (codigo != 0) {
            throw new BadRequestException(mensaje);
        }

        // Buscamos la entidad completa para que el mapper cargue nombres de
        // sala/usuario
        return reservaRepository.findById(idGenerado)
                .map(reservaMapper::toResponseDto)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Reserva creada pero no encontrada con ID: " + idGenerado));
    }

    @Override
    public void cancelarReserva(CancelarReservaRequestDto request) {
        StoredProcedureQuery q = entityManager.createStoredProcedureQuery("RESERVAS.pkg_reservas.pr_cancelar_reserva");

        q.registerStoredProcedureParameter("p_id_reserva", Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_motivo", String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter("p_codigo_salida", Integer.class, ParameterMode.OUT);
        q.registerStoredProcedureParameter("p_mensaje_salida", String.class, ParameterMode.OUT);

        q.setParameter("p_id_reserva", request.getIdReserva());
        q.setParameter("p_motivo", request.getMotivo());

        q.execute();

        Integer codigo = (Integer) q.getOutputParameterValue("p_codigo_salida");
        String mensaje = (String) q.getOutputParameterValue("p_mensaje_salida");

        if (codigo != 0) {
            throw new BadRequestException(mensaje);
        }
    }

    @Override
    public List<ReservaResponseDto> getDatosCalendario() {
        String usernameActual = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = hasRole(ROLE_ADMIN);

        return reservaRepository.findAllNotCancelled().stream()
                .map(reserva -> applyPrivacyFilter(reserva, usernameActual, isAdmin))
                .toList();
    }

    private ReservaResponseDto applyPrivacyFilter(ReservaEntity reserva, String usernameActual, boolean isAdmin) {
        if (isAdmin || reserva.getUsuario().getUsername().equals(usernameActual)) {
            return reservaMapper.toResponseDto(reserva);
        }

        return ReservaResponseDto.builder()
                .idReserva(reserva.getIdReserva())
                .nombreSala(reserva.getSala().getNombre())
                .username("Ocupado")
                .fechaInicio(reserva.getFechaInicio())
                .fechaFin(reserva.getFechaFin())
                .estado(reserva.getEstado())
                .build();
    }

    @Override
    public ReservaResponseDto findById(Long id) {
        return reservaRepository.findById(id)
                .map(reservaMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));
    }

    @Override
    public List<ReservaResponseDto> findAll() {
        return reservaMapper.toResponseDtoList(reservaRepository.findAll());
    }

    @Override
    public List<ReservaResponseDto> findByUsername(String username) {
        return reservaMapper.toResponseDtoList(reservaRepository.findByUsuarioUsername(username));
    }

    private boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }

    @Override
    public ReservaResponseDto update(Long id, CrearReservaRequestDto request) {
        throw new UnsupportedOperationException("Lógica de actualización no permitida directamente.");
    }

    @Override
    public ReservaResponseDto delete(Long id) {
        throw new UnsupportedOperationException("Use el método cancelarReserva para deshabilitar una reserva.");
    }
}