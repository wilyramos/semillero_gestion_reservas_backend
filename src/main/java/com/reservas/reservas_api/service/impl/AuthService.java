package com.reservas.reservas_api.service.impl;

import com.reservas.reservas_api.dto.AuthRequestDto;
import com.reservas.reservas_api.dto.AuthResponseDto;
import com.reservas.reservas_api.jwt.JwtUtil;
import com.reservas.reservas_api.models.UsuarioEntity;
import com.reservas.reservas_api.repository.UserRepository;
import com.reservas.reservas_api.repository.UsuarioRolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponseDto login(AuthRequestDto request) {
        // 1. Validar usuario
        UsuarioEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // 2. Validar password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // 3. Obtener roles de la DB
        List<String> roles = usuarioRolRepository.findByUsuario(user)
                .stream()
                .map(ur -> ur.getRol().getNombreRol())
                .collect(Collectors.toList());

        // 4. Generar Token con roles
        String token = jwtUtil.generateToken(user.getUsername(), roles);

        return AuthResponseDto.builder()
                .id(user.getIdUsuario())
                .token(token)
                .username(user.getUsername())
                .roles(roles)
                .build();
    }
}