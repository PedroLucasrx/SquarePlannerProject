package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.ProgressoConteudo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgressoConteudoRepository extends JpaRepository<ProgressoConteudo, Long> {

    Optional<ProgressoConteudo> findByUsuarioIdAndConteudoId(
        Long usuarioId,
        Long conteudoId
    );

    List<ProgressoConteudo> findByUsuarioId(Long usuarioId);

    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO progresso_conteudo (usuario_id, conteudo_id, concluido)
    SELECT u.id, :conteudoId, :concluido
    FROM usuarios u
    INNER JOIN conteudos c ON c.id = :conteudoId
    WHERE u.email = :email
    ON CONFLICT (usuario_id, conteudo_id)
    DO UPDATE SET concluido = EXCLUDED.concluido
    """, nativeQuery = true)
    int alterarEstado(
            @Param("email") String email,
            @Param("conteudoId") Long conteudoId,
            @Param("concluido") boolean concluido
    );
}
