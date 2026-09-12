package com.example.squarePlanner.enity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ano_escolar")
public class AnoEscolar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true)
    private Integer ordem;

    public AnoEscolar() {
    }

    public AnoEscolar(String nome, Integer ordem) {
        this.nome = nome;
        this.ordem = ordem;
    }


}