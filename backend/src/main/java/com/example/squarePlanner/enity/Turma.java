package com.example.squarePlanner.enity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "turma", uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_turma_nome_ano",
            columnNames = {"nome", "ano_escolar_id"}
        )})
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "ano_escolar_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_turma_ano_escolar")
    )
    private AnoEscolar anoEscolar;

    public Turma() {
    }

    public Turma(String nome, AnoEscolar anoEscolar) {
        this.nome = nome;
        this.anoEscolar = anoEscolar;
    }


}