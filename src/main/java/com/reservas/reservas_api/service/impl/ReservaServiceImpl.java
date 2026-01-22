package com.reservas.reservas_api.service.impl;

import com.reservas.reservas_api.commons.FilterModel;
import com.reservas.reservas_api.commons.PaginationModel;
import com.reservas.reservas_api.commons.SortModel;
import com.reservas.reservas_api.dto.*;
import com.reservas.reservas_api.exception.*;
import com.reservas.reservas_api.mappers.ReservaMapper;
import com.reservas.reservas_api.models.ReservaEntity;
import com.reservas.reservas_api.repository.IReservaRepository;
import com.reservas.reservas_api.service.IReservaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        // El mismo orden del package
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

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDto> findByFechaRange(LocalDateTime inicio, LocalDateTime fin, Long idSala) {
        String usernameActual = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = hasRole(ROLE_ADMIN);

        // Construir la consulta dinámica JPQL
        StringBuilder jpql = new StringBuilder(
                "SELECT r FROM ReservaEntity r WHERE r.fechaInicio >= :inicio AND r.fechaFin <= :fin");

        if (idSala != null) {
            jpql.append(" AND r.sala.idSala = :idSala");
        }

        TypedQuery<ReservaEntity> query = entityManager.createQuery(jpql.toString(), ReservaEntity.class);

        query.setParameter("inicio", inicio);
        query.setParameter("fin", fin);

        if (idSala != null) {
            query.setParameter("idSala", idSala);
        }

        // Ejecutar y aplicar filtro de privacidad (tu lógica existente)
        return query.getResultList().stream()
                .map(reserva -> applyPrivacyFilter(reserva, usernameActual, isAdmin))
                .toList();
    }

    @Override
    public DashboardStatsDto getAdminStats() {
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);

        // 1. Calcular Ocupación
        List<ReservaEntity> reservasMes = reservaRepository.findReservasMesActual(inicioMes);

        Map<Integer, Long> horasPico = reservasMes.stream()
                .collect(Collectors.groupingBy(r -> r.getFechaInicio().getHour(), Collectors.counting()));

        return DashboardStatsDto.builder()
                .totalReservasHoy(reservaRepository.countReservasHoy())
                .salaMayorDemanda(reservaRepository.findSalaMasDemandada())
                .horasPico(horasPico)
                .build();
    }

    // get reservas admin with pagination

    @Override
    @Transactional(readOnly = true)
    public PageImpl<ReservaResponseDto> getPagination(PaginationModel paginationModel) {
        int page = (paginationModel.getPageNumber() != null) ? paginationModel.getPageNumber() : 0;
        int size = (paginationModel.getRowsPerPage() != null && paginationModel.getRowsPerPage() > 0)
                ? paginationModel.getRowsPerPage()
                : 10;

        Pageable pageable = PageRequest.of(page, size);

        // Where para reutilizar en ambas consultas
        StringBuilder whereClause = new StringBuilder(" WHERE 1=1 ");

        if (paginationModel.getFilters() != null && !paginationModel.getFilters().isEmpty()) {
            for (FilterModel filter : paginationModel.getFilters()) {
                if (filter.getValue() == null || filter.getValue().toString().isEmpty())
                    continue;

                switch (filter.getColName()) {
                    case "estado" ->
                        whereClause.append(" AND r.estado = '").append(filter.getValue()).append("'");
                    case "username" ->
                        whereClause.append(" AND r.usuario.username LIKE '%").append(filter.getValue()).append("%'");
                    case "nombreSala" ->
                        whereClause.append(" AND r.sala.nombre LIKE '%").append(filter.getValue()).append("%'");

                    // Filtros por rango de fechas
                    case "fechaInicio" ->
                        whereClause.append(" AND r.fechaInicio >= TO_TIMESTAMP('").append(filter.getValue())
                                .append(" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')");
                    case "fechaFin" ->
                        whereClause.append(" AND r.fechaFin <= TO_TIMESTAMP('").append(filter.getValue())
                                .append(" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')");
                }
            }
        }

        // Consulta de Datos
        StringBuilder sql = new StringBuilder(
                "SELECT new com.reservas.reservas_api.dto.ReservaResponseDto(" +
                        "r.idReserva, r.sala.nombre, r.usuario.username, r.fechaInicio, r.fechaFin, r.estado) " +
                        "FROM ReservaEntity r ")
                .append(whereClause);

        // Ordenamiento
        if (paginationModel.getSorts() != null && !paginationModel.getSorts().isEmpty()) {
            sql.append(" ORDER BY ");
            for (int i = 0; i < paginationModel.getSorts().size(); i++) {
                SortModel sort = paginationModel.getSorts().get(i);
                String colName = switch (sort.getColName()) {
                    case "nombreSala" -> "r.sala.nombre";
                    case "username" -> "r.usuario.username";
                    default -> "r." + sort.getColName();
                };
                sql.append(colName).append(" ").append(sort.getDirection());
                if (i < paginationModel.getSorts().size() - 1)
                    sql.append(", ");
            }
        } else {
            sql.append(" ORDER BY r.fechaInicio DESC");
        }

        TypedQuery<ReservaResponseDto> querySelect = entityManager.createQuery(sql.toString(),
                ReservaResponseDto.class);
        querySelect.setFirstResult((int) pageable.getOffset());
        querySelect.setMaxResults(pageable.getPageSize());
        List<ReservaResponseDto> results = querySelect.getResultList();

        // Conteno
        String countSql = "SELECT COUNT(r) FROM ReservaEntity r " + whereClause;
        Long totalRegistros = entityManager.createQuery(countSql, Long.class).getSingleResult();

        return new PageImpl<>(results, pageable, totalRegistros);
    }

    @Override
    @Transactional(readOnly = true)
    public PageImpl<ReservaResponseDto> getPaginationByUser(String username, PaginationModel paginationModel) {
        int page = (paginationModel.getPageNumber() != null) ? paginationModel.getPageNumber() : 0;
        int size = (paginationModel.getRowsPerPage() != null && paginationModel.getRowsPerPage() > 0)
                ? paginationModel.getRowsPerPage()
                : 10;

        Pageable pageable = PageRequest.of(page, size);

        // Where base con filtro por usuario
        StringBuilder whereClause = new StringBuilder(" WHERE r.usuario.username = :username ");

        if (paginationModel.getFilters() != null && !paginationModel.getFilters().isEmpty()) {
            for (FilterModel filter : paginationModel.getFilters()) {
                if (filter.getValue() == null || filter.getValue().toString().isEmpty())
                    continue;

                switch (filter.getColName()) {
                    case "estado" ->
                        whereClause.append(" AND r.estado = '").append(filter.getValue()).append("'");
                    case "nombreSala" ->
                        whereClause.append(" AND r.sala.nombre LIKE '%").append(filter.getValue()).append("%'");

                    // Filtros por rango de fechas
                    case "fechaInicio" ->
                        whereClause.append(" AND r.fechaInicio >= TO_TIMESTAMP('").append(filter.getValue())
                                .append(" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')");
                    case "fechaFin" ->
                        whereClause.append(" AND r.fechaFin <= TO_TIMESTAMP('").append(filter.getValue())
                                .append(" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')");
                }
            }
        }

        // Consulta de Datos
        StringBuilder sql = new StringBuilder(
                "SELECT new com.reservas.reservas_api.dto.ReservaResponseDto(" +
                        "r.idReserva, r.sala.nombre, r.usuario.username, r.fechaInicio, r.fechaFin, r.estado) " +
                        "FROM ReservaEntity r ")
                .append(whereClause);

        // Ordenamiento
        if (paginationModel.getSorts() != null && !paginationModel.getSorts().isEmpty()) {
            sql.append(" ORDER BY ");
            for (int i = 0; i < paginationModel.getSorts().size(); i++) {
                SortModel sort = paginationModel.getSorts().get(i);
                String colName = switch (sort.getColName()) {
                    case "nombreSala" -> "r.sala.nombre";
                    default -> "r." + sort.getColName();
                };
                sql.append(colName).append(" ").append(sort.getDirection());
                if (i < paginationModel.getSorts().size() - 1)
                    sql.append(", ");
            }
        } else {
            sql.append(" ORDER BY r.fechaInicio DESC");
        }

        TypedQuery<ReservaResponseDto> querySelect = entityManager.createQuery(sql.toString(),
                ReservaResponseDto.class);
        querySelect.setParameter("username", username);
        querySelect.setFirstResult((int) pageable.getOffset());
        querySelect.setMaxResults(pageable.getPageSize());
        List<ReservaResponseDto> results = querySelect.getResultList();

        // Conteo
        String countSql = "SELECT COUNT(r) FROM ReservaEntity r " + whereClause;
        TypedQuery<Long> countQuery = entityManager.createQuery(countSql, Long.class);
        countQuery.setParameter("username", username);
        Long totalRegistros = countQuery.getSingleResult();

        return new PageImpl<>(results, pageable, totalRegistros);
    }
}