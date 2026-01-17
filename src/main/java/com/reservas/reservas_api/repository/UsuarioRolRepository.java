package com.reservas.reservas_api.repository;

import com.reservas.reservas_api.models.UsuarioEntity;
import com.reservas.reservas_api.models.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, Long> {
    
    // Obtiene la lista de roles asociados a un usuario específico.
    List<UsuarioRol> findByUsuario(UsuarioEntity usuario);

    // Elimina los roles asociados a un usuario específico.
    void deleteByUsuario(UsuarioEntity usuario);
}