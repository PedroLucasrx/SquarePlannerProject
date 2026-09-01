package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.Conteudo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConteudoRepository extends JpaRepository<Conteudo,Long> {
    List<Conteudo> findByProvaId(Long provaId);
    boolean existsByNome(String nome);
}
