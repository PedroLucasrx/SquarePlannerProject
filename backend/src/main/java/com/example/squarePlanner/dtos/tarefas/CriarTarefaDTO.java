package com.example.squarePlanner.dtos.tarefas;

import java.time.LocalDate;
import java.util.List;

public record CriarTarefaDTO(
        String materia,
        LocalDate data,
        int trimestre,
        List<CriarTarefaAtividadeDTO> atividades
) {
}
