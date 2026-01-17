package com.reservas.reservas_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class UsuarioRequestDto {
    private String username;
    private String password;
    private List<Long> roleIds;
}