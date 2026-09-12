package com.example.squarePlanner.service;

import com.example.squarePlanner.enity.Turma;
import com.example.squarePlanner.repository.TurmaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;

    public TurmaService(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    public List<Turma> listarTurmas() {
        return turmaRepository.findAllByOrderByAnoEscolarOrdemAscNomeAsc();
    }
}