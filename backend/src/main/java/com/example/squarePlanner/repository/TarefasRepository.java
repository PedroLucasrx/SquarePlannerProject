package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefasRepository extends JpaRepository<Tarefa,Long> {
    boolean existsByMateriaAndTrimestre(String materia,int trimestre);
    List<Tarefa> findByTurmaIdOrderByData(Long turmaId);
}
