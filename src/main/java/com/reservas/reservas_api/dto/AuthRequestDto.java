package com.reservas.reservas_api.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String username;
    private String password;
}