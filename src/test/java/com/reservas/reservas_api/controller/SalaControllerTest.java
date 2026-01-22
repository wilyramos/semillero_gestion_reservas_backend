package com.reservas.reservas_api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.reservas.reservas_api.dto.SalaRequestDto;
import com.reservas.reservas_api.dto.SalaResponseDto;
import com.reservas.reservas_api.service.ISalaService;

@ExtendWith(MockitoExtension.class)
public class SalaControllerTest {
    
    private final SalaResponseDto SALA_RESPONSE_DTO = new SalaResponseDto();
    private final SalaRequestDto SALA_REQUEST_DTO = new SalaRequestDto();

    @Mock
    ISalaService salaService;
    
    @InjectMocks
    SalaController salaController;

    @BeforeEach
    public void setUp() {
        SALA_RESPONSE_DTO.setIdSala(5L);
        SALA_RESPONSE_DTO.setNombre("Sala A");
        SALA_RESPONSE_DTO.setCapacidad(50);

        SALA_REQUEST_DTO.setNombre("Sala A");
        SALA_REQUEST_DTO.setCapacidad(50);
    }


    @Test
    public void create() {
       var expected = ResponseEntity.status(HttpStatus.CREATED).body(SALA_RESPONSE_DTO);
       when(salaService.save(SALA_REQUEST_DTO)).thenReturn(SALA_RESPONSE_DTO);

       ResponseEntity<SalaResponseDto> response = salaController.create(SALA_REQUEST_DTO);

       var id = response.getBody().getIdSala();
       assert(id > 1L);

       var statusCode = response.getStatusCode();
        assertEquals(expected.getStatusCode(), statusCode);

       assertEquals(expected, response);


    }
}
