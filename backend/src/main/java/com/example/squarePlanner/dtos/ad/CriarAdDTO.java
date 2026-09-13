package com.example.squarePlanner.dtos.ad;

import java.time.LocalDate;

public record CriarAdDTO(
        String materia,
        LocalDate data,
        int trimestre,
        String proposta
) {
}
