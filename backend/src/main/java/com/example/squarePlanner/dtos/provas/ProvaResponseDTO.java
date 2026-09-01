package com.example.squarePlanner.dtos.provas;

import com.example.squarePlanner.dtos.conteudos.ConteudoResponseDTO;

import java.time.LocalDate;
import java.util.List;

public record ProvaResponseDTO(
        Long id,
        String materia,
        LocalDate data,
        int trimestre,
        List<ConteudoResponseDTO> conteudos,

        int conteudosConcluidos,
        int totalConteudos,
        double progresso
) {
}
