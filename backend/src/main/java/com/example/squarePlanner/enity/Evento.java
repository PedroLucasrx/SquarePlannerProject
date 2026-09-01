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


    protected Evento(){}

    public Evento(String nome, LocalDate data){
        this.nome = nome;
        this.data = data;

    }
}
