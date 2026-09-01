package com.example.squarePlanner.enity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "conteudos")
public class Conteudo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;


    @Column(name = "prova_id")
    private Long provaId;

    protected Conteudo() {}

    public Conteudo(String nome, Long provaId) {
        this.provaId = provaId;
        this.nome = nome;
    }

}
