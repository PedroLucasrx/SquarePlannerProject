package com.example.squarePlanner.enity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String materia;
    private LocalDate data;
    private int trimestre;

    protected Tarefa(){}

    public Tarefa(String materia, LocalDate data, int trimestre){
        this.materia = materia;
        this.data = data;
        this.trimestre = trimestre;
    }
}

