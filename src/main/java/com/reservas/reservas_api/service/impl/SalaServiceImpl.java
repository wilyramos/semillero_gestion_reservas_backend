package com.reservas.reservas_api.service.impl;

import com.reservas.reservas_api.dto.SalaRequestDto;
import com.reservas.reservas_api.dto.SalaResponseDto;
import com.reservas.reservas_api.exception.ConflictException;
import com.reservas.reservas_api.exception.ResourceNotFoundException;
import com.reservas.reservas_api.mappers.SalaMapper;
import com.reservas.reservas_api.models.SalaEntity;
import com.reservas.reservas_api.repository.SalaRepository;
import com.reservas.reservas_api.service.ISalaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaServiceImpl implements ISalaService {

    private final SalaRepository salaRepository;
    private final SalaMapper salaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SalaResponseDto> findAll() {
        return salaMapper.toResponseDtoList(salaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public SalaResponseDto findById(Long id) {
        return salaRepository.findById(id)
                .map(salaMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada"));
    }

    @Override
    @Transactional
    public SalaResponseDto save(SalaRequestDto request) {
        if (salaRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new ConflictException("El nombre de la sala ya existe");
        }
        SalaEntity entity = salaMapper.toEntity(request);
        return salaMapper.toResponseDto(salaRepository.save(entity));
    }

    @Override
    @Transactional
    public SalaResponseDto update(Long id, SalaRequestDto request) {
        SalaEntity entity = salaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada"));
        
        salaMapper.updateEntity(entity, request);
        return salaMapper.toResponseDto(salaRepository.save(entity));
    }

    @Override
    @Transactional
    public SalaResponseDto delete(Long id) {
        SalaEntity entity = salaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar: Sala no encontrada"));
        
        SalaResponseDto response = salaMapper.toResponseDto(entity);
        salaRepository.delete(entity);
        return response; // Retornamos el DTO de lo que fue eliminado
    }
}