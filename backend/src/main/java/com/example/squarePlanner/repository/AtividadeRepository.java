package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtividadeRepository extends JpaRepository<Atividade,Long> {
    List<Atividade> findByTarefaId(Long tarefa_id);
    boolean existsByNome(String nome);
}
