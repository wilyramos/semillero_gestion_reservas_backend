package com.reservas.reservas_api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROL")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rol_generator")
    @SequenceGenerator(
        name = "rol_generator", 
        sequenceName = "seq_rol", 
        allocationSize = 1
    )
    @Column(name = "ID_ROL")
    private Long idRol;

    @Column(name = "NOMBRE_ROL", unique = true, nullable = false)
    private String nombreRol;
}