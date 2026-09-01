package com.example.squarePlanner.dtos.ad;

import java.time.LocalDate;

public record AdResponseDTO(
        Long id,
        String materia,
        LocalDate data,
        int trimestre,
        String proposta,
        boolean concluido
) {
}
