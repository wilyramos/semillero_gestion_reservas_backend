package com.reservas.reservas_api.service;

import com.reservas.reservas_api.commons.ICrudCommons;
import com.reservas.reservas_api.commons.PaginationModel;
import com.reservas.reservas_api.dto.UsuarioRequestDto;
import com.reservas.reservas_api.dto.UsuarioResponseDto;
import java.util.List;

import org.springframework.data.domain.PageImpl;

public interface IUserService extends ICrudCommons<UsuarioResponseDto, Long> {
    UsuarioResponseDto saveRequest(UsuarioRequestDto dto);
    UsuarioResponseDto updateRequest(Long id, UsuarioRequestDto dto);
    List<UsuarioResponseDto> findAll();

    PageImpl<UsuarioResponseDto> getPagination(PaginationModel paginationModel);
}