package com.example.squarePlanner.dtos.usuario;

public record LoginGoogleResponseDTO(
        String credential,
        boolean precisaCadastro
) {
}