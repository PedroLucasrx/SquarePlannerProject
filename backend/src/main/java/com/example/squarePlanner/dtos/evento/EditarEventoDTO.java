package com.example.squarePlanner.dtos.evento;

import java.time.LocalDate;

public record EditarEventoDTO(
        String nome,
        LocalDate data
) {
}
