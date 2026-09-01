package com.example.squarePlanner.dtos.provas;

import com.example.squarePlanner.dtos.conteudos.EditarConteudoDTO;

import java.time.LocalDate;
import java.util.List;

public record EditarProvaDTO(
        String materia,
        LocalDate data,
        int trimestre,
        List<EditarConteudoDTO> conteudos
) {
}
