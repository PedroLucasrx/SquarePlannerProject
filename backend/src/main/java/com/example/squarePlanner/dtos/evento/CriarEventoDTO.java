package com.example.squarePlanner.dtos.evento;

import com.example.squarePlanner.enity.Turma;

import java.time.LocalDate;

public record CriarEventoDTO(
        String nome,
        LocalDate data,
        Turma turma
) {}
