package com.reserva.dto;

public record SalaResponseDTO(
    Long idSala,
    String nome,
    String descricao,
    int capacidade,
    boolean ativo
) {
    
}
