package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.ProgressoAtividades;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgressoAtividadesRepository extends JpaRepository<ProgressoAtividades, Long> {

    Optional<ProgressoAtividades> findByUsuarioIdAndAtividadesId(
            Long usuarioId,
            Long atividadesId
    );

    List<ProgressoAtividades> findByUsuarioId(Long usuarioId);

    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO progresso_atividades (usuario_id, atividades_id, concluido)
    SELECT u.id, :atividadeId, :concluido
    FROM usuarios u
    INNER JOIN atividades a ON a.id = :atividadeId
    WHERE u.email = :email
    ON CONFLICT (usuario_id, atividades_id)
    DO UPDATE SET concluido = EXCLUDED.concluido
    """, nativeQuery = true)
    int alterarEstado(
            @Param("email") String email,
            @Param("atividadeId") Long atividadeId,
            @Param("concluido") boolean concluido
    );
}
