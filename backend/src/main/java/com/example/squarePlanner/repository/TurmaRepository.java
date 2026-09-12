package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurmaRepository extends JpaRepository<Turma, Long> {

    List<Turma> findByAnoEscolarIdOrderByNome(Long anoEscolarId);

    List<Turma> findAllByOrderByAnoEscolarOrdemAscNomeAsc();
}