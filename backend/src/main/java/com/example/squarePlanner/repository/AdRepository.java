package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Ad,Long> {

    boolean existsByMateriaAndTrimestre(String materia,int trimestre);
}
