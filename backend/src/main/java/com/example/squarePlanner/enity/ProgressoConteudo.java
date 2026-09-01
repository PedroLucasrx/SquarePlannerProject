package com.example.squarePlanner.enity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "progresso_conteudo",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"usuario_id", "conteudo_id"})
        }
)
public class ProgressoConteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "conteudo_id", nullable = false)
    private Conteudo conteudo;

    @Column(nullable = false)
    private boolean concluido = false;

    public ProgressoConteudo() {
    }

}
