package com.example.squarePlanner.enity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "turma_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_evento_turma")
    )
    private Turma turma;

    protected Evento() {}

    public Evento(String nome, LocalDate data, Turma turma) {
        this.nome = nome;
        this.data = data;
        this.turma = turma;
    }
}