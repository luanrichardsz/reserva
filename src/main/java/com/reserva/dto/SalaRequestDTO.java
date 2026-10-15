package com.reserva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SalaRequestDTO(
    @NotBlank(message = "O nome é obrigatorio!")
    @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres!")
    String nome,

    @NotBlank(message = "A descrição é obrigatoria!")
    @Size(max = 70, message = "A descrição deve ter no máximo 70 caracteres!")
    String descricao,

    @Positive(message = "A capacidade deve ser um número positivo!")
    int capacidade
) {
    
}
