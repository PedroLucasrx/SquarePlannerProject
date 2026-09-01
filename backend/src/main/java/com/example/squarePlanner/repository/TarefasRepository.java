package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefasRepository extends JpaRepository<Tarefa,Long> {
    boolean existsByMateriaAndTrimestre(String materia,int trimestre);
}
