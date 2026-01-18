package com.reservas.reservas_api.repository;

import com.reservas.reservas_api.models.SalaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalaRepository extends JpaRepository<SalaEntity, Long> {


    // Método útil para validar duplicados antes de guardar o actualizar
    Optional<SalaEntity> findByNombre(String nombre);

    // 
}