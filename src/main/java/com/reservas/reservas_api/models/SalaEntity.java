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
@Table(name = "SALA")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sala_seq")
    @SequenceGenerator(name = "sala_seq", sequenceName = "SEQ_SALA", allocationSize = 1)
    private Long idSala;

    @Column(unique = true, nullable = false)
    private String nombre;

    private Integer capacidad;
}