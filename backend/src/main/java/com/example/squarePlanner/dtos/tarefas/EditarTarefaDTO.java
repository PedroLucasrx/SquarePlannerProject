package com.example.squarePlanner.dtos.tarefas;

import com.example.squarePlanner.dtos.atividades.EditarAtividadeDTO;

import java.time.LocalDate;
import java.util.List;

public record EditarTarefaDTO(
        String materia,
        LocalDate data,
        int trimestre,
        List<EditarAtividadeDTO> atividades
) {
}
