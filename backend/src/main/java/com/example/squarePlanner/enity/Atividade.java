package com.example.squarePlanner.enity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "atividades")
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;


    @Column(name = "tarefa_id")
    private Long tarefaId;

    protected Atividade() {}

    public Atividade(String nome, Long tarefaId) {
        this.tarefaId = tarefaId;
        this.nome = nome;
    }
}
