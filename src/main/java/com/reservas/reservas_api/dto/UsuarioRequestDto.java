package com.reservas.reservas_api.dto;

import lombok.Data;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Data
public class UsuarioRequestDto {
    @NotBlank(message = "El nombre de usuario no puede estar vacio")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacia")
    private String password;

    @NotEmpty(message = "Debe asignar al menos un rol al usuario")
    private List<Long> roleIds;
}