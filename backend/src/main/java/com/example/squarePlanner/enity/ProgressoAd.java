package com.example.squarePlanner.enity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table( name = "progresso_ad",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "ad_id"})
    }
)
public class ProgressoAd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;

    @Column(nullable = false)
    private boolean concluido = false;

    public ProgressoAd() {
    }

}

