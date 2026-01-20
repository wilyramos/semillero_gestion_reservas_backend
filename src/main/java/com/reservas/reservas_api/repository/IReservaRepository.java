package com.reservas.reservas_api.repository;

import com.reservas.reservas_api.models.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservaRepository extends JpaRepository<ReservaEntity, Long> {

    List<ReservaEntity> findByUsuarioUsername(String username);

    @Query("SELECT r FROM ReservaEntity r WHERE r.estado <> 'CANCELADA'")
    List<ReservaEntity> findAllNotCancelled();
}