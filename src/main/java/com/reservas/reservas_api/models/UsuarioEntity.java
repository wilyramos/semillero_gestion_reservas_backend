package com.reservas.reservas_api.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USUARIO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {

    // Revisar el sequencer para usuario
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_generator")
    @SequenceGenerator(
        name = "usuario_generator", 
        sequenceName = "seq_usuario", 
        allocationSize = 1
    )
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
}