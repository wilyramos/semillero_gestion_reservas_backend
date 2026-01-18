package com.reservas.reservas_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sala_generator")
    @SequenceGenerator(
        name = "sala_generator",
        sequenceName = "seq_sala",
        allocationSize = 1
    )
    @Column(name = "ID_SALA")
    private Long idSala;

    @NotBlank(message = "El nombre de la sala es obligatorio")
    private String nombre;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    private Integer capacidad;
}