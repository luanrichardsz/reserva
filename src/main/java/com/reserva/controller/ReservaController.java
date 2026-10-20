package com.reserva.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reserva.dto.ReservaRequestDTO;
import com.reserva.dto.ReservaResponseDTO;
import com.reserva.model.StatusReserva;
import com.reserva.service.ReservaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity <ReservaResponseDTO> cadastrar(@Valid @RequestBody ReservaRequestDTO dto){
        ReservaResponseDTO reservaSalva = reservaService.cadastrar(dto);
        return ResponseEntity.status(201).body(reservaSalva);
    }

    @GetMapping
    public ResponseEntity <List<ReservaResponseDTO>> listarTodos(){
        List<ReservaResponseDTO> reservas = reservaService.listarTodas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/data")
    public ResponseEntity <List<ReservaResponseDTO>> listarPorData(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data){
        List<ReservaResponseDTO> reservas = reservaService.listarReservasPorData(data);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/status")
    public ResponseEntity <List<ReservaResponseDTO>> listarPorStatus(@RequestParam StatusReserva status){
        List<ReservaResponseDTO> reservas = reservaService.listarReservasPorStatus(status);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity <List<ReservaResponseDTO>> listarPorUsuario(@PathVariable Long idUsuario){
        List<ReservaResponseDTO> reservas = reservaService.listarReservasDoUsuario(idUsuario);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/sala/{idSala}")
    public ResponseEntity <List<ReservaResponseDTO>> listarPorSala(@PathVariable Long idSala){
        List<ReservaResponseDTO> reservas = reservaService.listarReservasDaSala(idSala);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{idReserva}")
    public ResponseEntity <ReservaResponseDTO> buscarPorReserva(@PathVariable Long idReserva){
        ReservaResponseDTO reserva = reservaService.buscarPorId(idReserva);
        return ResponseEntity.ok(reserva);        
    }

    @PatchMapping("/{idReserva}/cancelar")
    public ResponseEntity <ReservaResponseDTO> cancelarReserva(@PathVariable Long idReserva){
        ReservaResponseDTO reserva = reservaService.cancelarReserva(idReserva);
        return ResponseEntity.ok(reserva);        
    }
}
