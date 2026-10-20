package com.reserva.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reserva.dto.SalaRequestDTO;
import com.reserva.dto.SalaResponseDTO;
import com.reserva.service.SalaService;

import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;

@RequiredArgsConstructor
@RestController 
@RequestMapping("/salas")
public class SalaController {
    private final SalaService salaService;

    @PostMapping 
    public ResponseEntity<SalaResponseDTO> cadastrar(@Valid @RequestBody SalaRequestDTO dto){
        SalaResponseDTO salaSalva = salaService.cadastrar(dto);

        return ResponseEntity.status(201).body(salaSalva);
    }

    @GetMapping 
    public ResponseEntity<List<SalaResponseDTO>> listarTodos(){
        List<SalaResponseDTO> salas = salaService.listarTodos();
        return ResponseEntity.ok(salas);
    }
    
    @PatchMapping("/{idSala}/interditar")
    public ResponseEntity<SalaResponseDTO> interditar(@PathVariable Long idSala){
        SalaResponseDTO interditarSala = salaService.interditar(idSala);
        return ResponseEntity.ok(interditarSala);
    }
}
