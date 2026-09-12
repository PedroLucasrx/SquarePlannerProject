package com.example.squarePlanner.enity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "provas")
public class Prova {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String materia;

    private LocalDate data;

    private int trimestre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "ano_escolar_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prova_ano_escolar")
    )
    private AnoEscolar anoEscolar;

    protected Prova() {}

    public Prova(
            String materia,
            LocalDate data,
            int trimestre,
            AnoEscolar anoEscolar
    ) {
        this.materia = materia;
        this.data = data;
        this.trimestre = trimestre;
        this.anoEscolar = anoEscolar;
    }
}