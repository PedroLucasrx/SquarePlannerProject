package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.AnoEscolar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnoEscolarRepository extends JpaRepository<AnoEscolar, Long> {

    Optional<AnoEscolar> findByOrdem(Integer ordem);
}