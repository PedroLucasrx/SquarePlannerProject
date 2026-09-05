package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.ProgressoAd;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgressoAdRepository extends JpaRepository<ProgressoAd,Long> {

    Optional<ProgressoAd> findByUsuarioIdAndAdId(
            Long usuarioId,
            Long adId
    );

    List<ProgressoAd> findByUsuarioId(Long usuarioId);
    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO progresso_ad (usuario_id, ad_id, concluido)
    SELECT u.id, :adId, :concluido
    FROM usuarios u
    INNER JOIN ad a ON a.id = :adId
    WHERE u.email = :email
    ON CONFLICT (usuario_id, ad_id)
    DO UPDATE SET concluido = EXCLUDED.concluido
    """, nativeQuery = true)
    int alterarEstado(
            @Param("email") String email,
            @Param("adId") Long adId,
            @Param("concluido") boolean concluido
    );
}
