package com.example.squarePlanner.dtos.ad;

import com.example.squarePlanner.enity.Turma;

import java.time.LocalDate;

public record CriarAdDTO(
        String materia,
        LocalDate data,
        int trimestre,
        String proposta,
        Turma turma
) {
}
