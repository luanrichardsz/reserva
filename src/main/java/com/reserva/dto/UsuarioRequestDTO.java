package com.reserva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO( 
    @NotBlank(message = "O nome é obrigatorio!")
    @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres!")
    String nome,

    @NotBlank(message = "O cargo é obrigatorio!")
    @Size(max = 50, message = "O cargo deve ter no máximo 50 caracteres!")
    String cargo,

    @NotBlank(message = "O setor é obrigatorio!")
    @Size(max = 30, message = "O setor deve ter no máximo 30 caracteres!")
    String setor
){}
