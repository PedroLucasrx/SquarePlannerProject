package com.example.squarePlanner.dtos.ad;

import java.time.LocalDate;

public record EditarAdDTO (
        String materia,
        LocalDate data,
        int trimestre,
        String proposta
) {
}
