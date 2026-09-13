package com.example.squarePlanner.dtos.usuario;

public record UsuarioLogadoDTO(
        String nome,
        String email,
        String role,
        Long turmaId
) {}
