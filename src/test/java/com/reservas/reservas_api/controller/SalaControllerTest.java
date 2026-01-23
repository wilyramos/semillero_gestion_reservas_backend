package com.reservas.reservas_api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.reservas.reservas_api.dto.SalaRequestDto;
import com.reservas.reservas_api.dto.SalaResponseDto;
import com.reservas.reservas_api.exception.ConflictException;
import com.reservas.reservas_api.exception.ResourceNotFoundException;
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
    public void getAll() {
        var expectedList = List.of(SALA_RESPONSE_DTO);
        when(salaService.findAll()).thenReturn(expectedList);

        ResponseEntity<List<SalaResponseDto>> response = salaController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
    }

    @Test
    public void getById() {
        when(salaService.findById(5L)).thenReturn(SALA_RESPONSE_DTO);

        ResponseEntity<SalaResponseDto> response = salaController.getById(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(SALA_RESPONSE_DTO, response.getBody());
    }

    @Test
    public void create() {
        var expected = ResponseEntity.status(HttpStatus.CREATED).body(SALA_RESPONSE_DTO);
        when(salaService.save(SALA_REQUEST_DTO)).thenReturn(SALA_RESPONSE_DTO);

        ResponseEntity<SalaResponseDto> response = salaController.create(SALA_REQUEST_DTO);

        assertEquals(expected.getStatusCode(), response.getStatusCode());
        assertEquals(SALA_RESPONSE_DTO.getIdSala(), response.getBody().getIdSala());
        assertEquals(expected, response);
    }

    @Test
    public void update() {
        var updated = new SalaResponseDto();
        updated.setIdSala(5L);
        updated.setNombre("Sala B");
        updated.setCapacidad(60);

        when(salaService.update(5L, SALA_REQUEST_DTO)).thenReturn(updated);

        ResponseEntity<SalaResponseDto> response = salaController.update(5L, SALA_REQUEST_DTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }

    @Test
    public void delete() {
        when(salaService.delete(5L)).thenReturn(SALA_RESPONSE_DTO);

        ResponseEntity<SalaResponseDto> response = salaController.delete(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(SALA_RESPONSE_DTO, response.getBody());
    }

    // Tests de casos de error

    @Test
    public void createDuplicateSala() {
        when(salaService.save(SALA_REQUEST_DTO))
                .thenThrow(new ConflictException("El nombre de la sala ya existe"));

        assertThrows(ConflictException.class, () -> salaController.create(SALA_REQUEST_DTO));
    }

    @Test
    public void getByIdNotFound() {
        when(salaService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Sala no encontrada"));

        assertThrows(ResourceNotFoundException.class, () -> salaController.getById(999L));
    }

    @Test
    public void updateSalaNotFound() {
        when(salaService.update(999L, SALA_REQUEST_DTO))
                .thenThrow(new ResourceNotFoundException("Sala no encontrada"));

        assertThrows(ResourceNotFoundException.class, 
                () -> salaController.update(999L, SALA_REQUEST_DTO));
    }

    @Test
    public void deleteSalaNotFound() {
        when(salaService.delete(999L))
                .thenThrow(new ResourceNotFoundException("No se puede eliminar: Sala no encontrada"));

        assertThrows(ResourceNotFoundException.class, () -> salaController.delete(999L));
    }

    @Test
    public void createWithInvalidCapacity() {
        SALA_REQUEST_DTO.setCapacidad(-1);
        when(salaService.save(SALA_REQUEST_DTO))
                .thenThrow(new IllegalArgumentException("La capacidad debe ser mayor a cero"));

        assertThrows(IllegalArgumentException.class, () -> salaController.create(SALA_REQUEST_DTO));
    }
}
