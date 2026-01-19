package com.reservas.reservas_api.service.impl;

import com.reservas.reservas_api.dto.UsuarioRequestDto;
import com.reservas.reservas_api.dto.UsuarioResponseDto;
import com.reservas.reservas_api.exception.BadRequestException;
import com.reservas.reservas_api.exception.ConflictException;
import com.reservas.reservas_api.exception.ResourceNotFoundException;
import com.reservas.reservas_api.mappers.UsuarioMapper;
import com.reservas.reservas_api.models.Rol;
import com.reservas.reservas_api.models.UsuarioEntity;
import com.reservas.reservas_api.models.UsuarioRol;
import com.reservas.reservas_api.repository.RolRepository;
import com.reservas.reservas_api.repository.UserRepository;
import com.reservas.reservas_api.repository.UsuarioRolRepository;
import com.reservas.reservas_api.service.IUserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> findAll() {
        return usuarioRepository.findAll().stream()
                .map(u -> usuarioMapper.toResponseDto(u, usuarioRolRepository.findByUsuario(u)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDto findById(Long id) {
        UsuarioEntity user = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return usuarioMapper.toResponseDto(user, usuarioRolRepository.findByUsuario(user));
    }

    @Override
    @Transactional
    public UsuarioResponseDto saveRequest(UsuarioRequestDto dto) {

        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new ConflictException("El usuario ya existe");
        }

        UsuarioEntity user = usuarioMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        UsuarioEntity savedUser = usuarioRepository.save(user);
        
        List<UsuarioRol> roles = saveUserRoles(savedUser, dto.getRoleIds());
        return usuarioMapper.toResponseDto(savedUser, roles);
    }

    @Override
    @Transactional
    public UsuarioResponseDto updateRequest(Long id, UsuarioRequestDto dto) {
        UsuarioEntity user = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        usuarioRolRepository.deleteByUsuario(user);
        List<UsuarioRol> roles = saveUserRoles(user, dto.getRoleIds());
        
        return usuarioMapper.toResponseDto(usuarioRepository.save(user), roles);
    }

    @Override
    @Transactional
    public UsuarioResponseDto delete(Long id) {
        UsuarioEntity user = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        UsuarioResponseDto response = usuarioMapper.toResponseDto(user, usuarioRolRepository.findByUsuario(user));
        
        usuarioRolRepository.deleteByUsuario(user);
        usuarioRepository.delete(user);
        return response;
    }

    private List<UsuarioRol> saveUserRoles(UsuarioEntity user, List<Long> roleIds) {
        List<UsuarioRol> roles = roleIds.stream().map(roleId -> {
            Rol rol = rolRepository.findById(roleId)
                    .orElseThrow(() -> new BadRequestException("Rol inválido: el rol con ID " + roleId + " no existe"));
            return UsuarioRol.builder().usuario(user).rol(rol).build();
        }).collect(Collectors.toList());
        return usuarioRolRepository.saveAll(roles);
    }

    // 
    @Override public UsuarioResponseDto save(UsuarioResponseDto entity) { return null; }
    @Override public UsuarioResponseDto update(Long id, UsuarioResponseDto entity) { return null; }
}