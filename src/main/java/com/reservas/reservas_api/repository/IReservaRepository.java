package com.reservas.reservas_api.repository;

import com.reservas.reservas_api.models.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IReservaRepository extends JpaRepository<ReservaEntity, Long> {

    List<ReservaEntity> findByUsuarioUsername(String username);

    @Query("SELECT r FROM ReservaEntity r WHERE r.estado <> 'CANCELADA'")
    List<ReservaEntity> findAllNotCancelled();

    // Filtro por fecha
    @Query("SELECT r FROM ReservaEntity r WHERE r.fechaInicio >= :inicio AND r.fechaFin <= :fin AND r.estado <> 'CANCELADA'")
    List<ReservaEntity> findByFechaRange(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    // Para Dashboard Stats

    @Query(value = "SELECT COUNT(*) FROM reserva " +
            "WHERE TRUNC(fecha_inicio) = TRUNC(SYSDATE) " +
            "AND estado <> 'CANCELADA'", nativeQuery = true)
    long countReservasHoy();

    @Query("SELECT r.sala.nombre FROM ReservaEntity r WHERE r.estado <> 'CANCELADA' GROUP BY r.sala.nombre ORDER BY COUNT(r) DESC LIMIT 1")
    String findSalaMasDemandada();

    @Query("SELECT r FROM ReservaEntity r WHERE r.fechaInicio >= :inicioMes AND r.estado <> 'CANCELADA'")
    List<ReservaEntity> findReservasMesActual(@Param("inicioMes") LocalDateTime inicioMes);
}