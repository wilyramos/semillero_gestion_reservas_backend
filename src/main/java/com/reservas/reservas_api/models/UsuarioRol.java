package com.reservas.reservas_api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USUARIO_ROL")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UsuarioRol {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "urol_generator")
    @SequenceGenerator(
        name = "urol_generator", 
        sequenceName = "seq_usuario_rol", 
        allocationSize = 1
    )
    @Column(name = "ID_USUARIO_ROL")
    private Long idUsuarioRol;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "ID_ROL", nullable = false)
    private Rol rol;
}