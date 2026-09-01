package com.example.squarePlanner.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final String secret =
            "uma-chave-secreta-muito-grande-para-meu-projeto-123456";

    private final SecretKey key =
            Keys.hmacShaKeyFor(
                    secret.getBytes(StandardCharsets.UTF_8)
            );

    public String gerarToken(
            String email,
            String nome,
            String role
    ) {

        return Jwts.builder()
                .subject(email)
                .claim("nome", nome)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis() + 86400000
                        )
                )
                .signWith(key)
                .compact();
    }

    public String extrairEmail(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}

