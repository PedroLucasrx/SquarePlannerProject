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

    @Column(unique = true)
    private String googleId;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "turma_id",
            foreignKey = @ForeignKey(name = "fk_usuario_turma")
    )
    private Turma turma;

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
