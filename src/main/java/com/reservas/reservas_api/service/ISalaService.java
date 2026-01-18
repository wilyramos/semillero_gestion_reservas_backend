package com.reservas.reservas_api.service;

import java.util.List;

import com.reservas.reservas_api.commons.ICrudCommonsDto;
import com.reservas.reservas_api.dto.SalaRequestDto;
import com.reservas.reservas_api.dto.SalaResponseDto;

public interface ISalaService extends ICrudCommonsDto<SalaRequestDto, SalaResponseDto, Long> {
    // Método adicional no incluido en el genérico
    List<SalaResponseDto> findAll();
}