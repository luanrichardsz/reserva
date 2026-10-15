package com.reserva.dto;

public record UsuarioResponseDTO(
    Long idUsuario,
    String nome,
    String cargo,
    String setor
){}