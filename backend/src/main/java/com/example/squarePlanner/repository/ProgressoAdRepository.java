package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.ProgressoAd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressoAdRepository extends JpaRepository<ProgressoAd,Long> {

    Optional<ProgressoAd> findByUsuarioIdAndAdId(
            Long usuarioId,
            Long adId
    );

    List<ProgressoAd> findByUsuarioId(Long usuarioId);
}
