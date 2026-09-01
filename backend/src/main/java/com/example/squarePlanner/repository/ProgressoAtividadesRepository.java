package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.ProgressoAtividades;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressoAtividadesRepository extends JpaRepository<ProgressoAtividades, Long> {

    Optional<ProgressoAtividades> findByUsuarioIdAndAtividadesId(
            Long usuarioId,
            Long atividadesId
    );

    List<ProgressoAtividades> findByUsuarioId(Long usuarioId);
}
