package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.Prova;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProvaRepository extends JpaRepository<Prova,Long> {

    boolean existsByMateriaAndTrimestre(String materia,int trimestre);
    @Query("SELECT p.id FROM Prova p WHERE p.materia = :materia")
    Optional<Long> findIdByMateria(@Param("materia") String materia);
}
