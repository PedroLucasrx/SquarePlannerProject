package com.example.squarePlanner.dtos.provas;

import com.example.squarePlanner.dtos.conteudos.CriarConteudoDTO;


import java.time.LocalDate;
import java.util.List;

public record CriarProvaDTO (
        String materia,
        LocalDate data,
        int trimestre,
        List<CriarConteudoDTO> conteudos
){
}
