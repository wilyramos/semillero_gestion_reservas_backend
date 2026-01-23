package com.reservas.reservas_api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.reservas.reservas_api.dto.UsuarioRequestDto;
import com.reservas.reservas_api.dto.UsuarioResponseDto;
import com.reservas.reservas_api.service.IUserService;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    private final UsuarioResponseDto USUARIO_RESPONSE_DTO = new UsuarioResponseDto();
    private final UsuarioRequestDto USUARIO_REQUEST_DTO = new UsuarioRequestDto();

    @Mock
    private IUserService userService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        USUARIO_RESPONSE_DTO.setIdUsuario(1L);
        USUARIO_RESPONSE_DTO.setUsername("usuario1");
        USUARIO_RESPONSE_DTO.setRoleNames(List.of("USER"));

        USUARIO_REQUEST_DTO.setUsername("usuario1");
        USUARIO_REQUEST_DTO.setPassword("password123");
        USUARIO_REQUEST_DTO.setRoleIds(List.of(1L));
    }

    @Test
    void create() {
        when(userService.saveRequest(USUARIO_REQUEST_DTO)).thenReturn(USUARIO_RESPONSE_DTO);

        ResponseEntity<UsuarioResponseDto> response = usuarioController.create(USUARIO_REQUEST_DTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(USUARIO_RESPONSE_DTO, response.getBody());
    }

    @Test
    void update() {
        var usuarioActualizado = new UsuarioResponseDto();
        usuarioActualizado.setIdUsuario(1L);
        usuarioActualizado.setUsername("usuarioActualizado");
        usuarioActualizado.setRoleNames(List.of("ADMIN"));

        when(userService.updateRequest(1L, USUARIO_REQUEST_DTO)).thenReturn(usuarioActualizado);

        ResponseEntity<UsuarioResponseDto> response = usuarioController.update(1L, USUARIO_REQUEST_DTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioActualizado, response.getBody());
    }

    @Test
    void delete() {
        when(userService.delete(1L)).thenReturn(USUARIO_RESPONSE_DTO);

        ResponseEntity<UsuarioResponseDto> response = usuarioController.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(USUARIO_RESPONSE_DTO, response.getBody());
    }
}
