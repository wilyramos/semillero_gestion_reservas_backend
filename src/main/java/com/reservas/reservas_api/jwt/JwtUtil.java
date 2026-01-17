package com.reservas.reservas_api.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret:my_secret_key_2026}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    public String generateToken(String username, List<String> roles) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(username)
                .withClaim("roles", roles) // Inyectamos roles en el token
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(algorithm);
    }

    public String extractUsername(String token) {
        return decodeToken(token).getSubject();
    }

    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getClaim("roles").asList(String.class)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJWT = decodeToken(token);
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}