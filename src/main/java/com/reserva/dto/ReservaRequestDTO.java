package com.reserva.dto;

import java.time.Instant;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record ReservaRequestDTO(
    @NotNull(message = "O ID da sala é obrigatorio!")
    Long idSala,

    @NotNull(message = "O ID do usuário é obrigatorio!")
    Long idUsuario,

    @NotNull(message = "A data de inicio é obrigatoria!")
    @Future(message = "A data de inicio deve ser uma data futura!")
    Instant dataInicio,

    @NotNull(message = "A data de fim é obrigatoria!")
    @Future(message = "A data de fim deve ser uma data futura!")
    Instant dataFim
){
}