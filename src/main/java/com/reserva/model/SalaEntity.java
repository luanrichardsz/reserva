package com.reserva.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sala")
@Data
@Builder 
@NoArgsConstructor
@AllArgsConstructor
public class SalaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSala;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private int capacidade;

    @Column(nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    private boolean ativo = true;
}
