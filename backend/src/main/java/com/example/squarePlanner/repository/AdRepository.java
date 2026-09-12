package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad,Long> {

    boolean existsByMateriaAndTrimestre(String materia,int trimestre);

    List<Ad> findByTurmaIdOrderByData(Long turmaId);
}
