package com.example.squarePlanner.dtos.evento;

import java.time.LocalDate;

public record CriarEventoDTO(
        String nome,
        LocalDate data
) {}
