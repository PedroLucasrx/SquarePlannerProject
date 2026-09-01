package com.example.squarePlanner.repository;

import com.example.squarePlanner.enity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface EventoRepository extends JpaRepository<Evento,Long> {

    boolean existsByNomeAndData(String nome, LocalDate data);
}
