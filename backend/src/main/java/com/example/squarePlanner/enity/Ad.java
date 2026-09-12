package com.example.squarePlanner.enity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ad")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String materia;

    private LocalDate data;

    private int trimestre;

    private String proposta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "turma_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_ad_turma")
    )
    private Turma turma;

    protected Ad() {}

    public Ad(
            String materia,
            LocalDate data,
            int trimestre,
            String proposta,
            Turma turma
    ) {
        this.materia = materia;
        this.data = data;
        this.trimestre = trimestre;
        this.proposta = proposta;
        this.turma = turma;
    }
}