package com.example.squarePlanner.dtos.tarefas;

import com.example.squarePlanner.dtos.atividades.AtividadesResponseDTO;

import java.time.LocalDate;
import java.util.List;

public record TarefaResponseDTO(
        Long id,
        String materia,
        LocalDate data,
        Integer trimestre,
        List<AtividadesResponseDTO> atividades,
        int atividadesConcluidas,
        int totalAtividades,
        double progresso
) {}
