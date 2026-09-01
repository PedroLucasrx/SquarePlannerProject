package com.example.squarePlanner.enity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table( name = "progresso_atividades",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"usuario_id", "atividades_id"})
        }
)
public class ProgressoAtividades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "atividades_id", nullable = false)
    private Atividade atividades;

    @Column(nullable = false)
    private boolean concluido = false;

    public ProgressoAtividades() {
    }

}

