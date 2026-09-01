package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.ProgressoConteudo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressoConteudoRepository extends JpaRepository<ProgressoConteudo, Long> {

    Optional<ProgressoConteudo> findByUsuarioIdAndConteudoId(
        Long usuarioId,
        Long conteudoId
    );

    List<ProgressoConteudo> findByUsuarioId(Long usuarioId);
}
