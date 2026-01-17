package com.reservas.reservas_api.models;

import java.time.LocalDateTime;

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
@Table(name = "RESERVA")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reserva_seq")
    @SequenceGenerator(name = "reserva_seq", sequenceName = "SEQ_RESERVA", allocationSize = 1)
    private Long idReserva;

    @ManyToOne
    @JoinColumn(name = "ID_SALA", nullable = false)
    private SalaEntity sala;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", nullable = false)

    private UsuarioEntity usuario;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estado;
}
