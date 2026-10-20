package com.reserva.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reserva.dto.UsuarioRequestDTO;
import com.reserva.dto.UsuarioResponseDTO;
import com.reserva.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity <UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO dto){
        UsuarioResponseDTO usuarioSalvo = usuarioService.cadastrar(dto);

        return ResponseEntity.status(201).body(usuarioSalvo);
    }

    @GetMapping
    public ResponseEntity <List<UsuarioResponseDTO>> listarTodos(){
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }
}
