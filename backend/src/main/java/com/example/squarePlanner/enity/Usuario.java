package com.example.squarePlanner.enity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String role;

    public Usuario() {
    }

    public Usuario(
            String nome,
            String email,
            String senha
    ) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = "USER";
    }

    // getters e setters
}
