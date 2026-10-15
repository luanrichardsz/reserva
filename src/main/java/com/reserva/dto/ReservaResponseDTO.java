package com.reserva.dto;

import java.time.Instant;

import com.reserva.model.StatusReserva;

public record ReservaResponseDTO(
    Long idReserva,
    Long idSala,
    Long idUsuario,
    Instant dataInicio,
    Instant dataFim,
    StatusReserva status
){ 
}
