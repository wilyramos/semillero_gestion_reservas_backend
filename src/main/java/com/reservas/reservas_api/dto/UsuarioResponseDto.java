package com.reservas.reservas_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDto {
    private Long idUsuario;
    private String username;
    private List<String> roleNames;
}