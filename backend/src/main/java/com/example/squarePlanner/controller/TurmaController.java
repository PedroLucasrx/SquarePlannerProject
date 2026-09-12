package com.example.squarePlanner.controller;

import com.example.squarePlanner.enity.Turma;
import com.example.squarePlanner.service.TurmaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/turmas")
public class TurmaController {

    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @GetMapping
    public List<Turma> listarTurmas() {
        return turmaService.listarTurmas();
    }
}