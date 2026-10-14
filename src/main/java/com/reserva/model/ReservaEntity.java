package com.reserva.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "reserva")
@Data
@Builder 
@NoArgsConstructor
@AllArgsConstructor
public class ReservaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private SalaEntity sala;

    @ManyToOne 
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "inicio", nullable = false)
    private Instant dataInicio;

    @Column(name = "fim", nullable = false)
    private Instant dataFim;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private StatusReserva status = StatusReserva.CONFIRMADO;
}