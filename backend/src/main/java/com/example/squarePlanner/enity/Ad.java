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

    protected Ad(){}

    public Ad(String materia, LocalDate data, int trimestre,String proposta){
        this.materia = materia;
        this.data = data;
        this.trimestre = trimestre;
        this.proposta = proposta;
    }
}
