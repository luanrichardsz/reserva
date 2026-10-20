package com.reserva.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Service;

import com.reserva.dto.ReservaRequestDTO;
import com.reserva.dto.ReservaResponseDTO;
import com.reserva.exception.RecursoConflitantes;
import com.reserva.exception.RecursoNaoEncontrado;
import com.reserva.model.ReservaEntity;
import com.reserva.model.SalaEntity;
import com.reserva.model.StatusReserva;
import com.reserva.model.UsuarioEntity;
import com.reserva.repository.ReservaRepository;
import com.reserva.repository.SalaRepository;
import com.reserva.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class ReservaService {
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;
    private final SalaRepository salaRepository;
    
    @Transactional 
    public ReservaResponseDTO cadastrar(ReservaRequestDTO dto){
        Long idSala = dto.idSala();
        Long idUsuario = dto.idUsuario();
        Instant dataInicio = dto.dataInicio();
        Instant dataFim = dto.dataFim();
        StatusReserva status = StatusReserva.CONFIRMADO;
        
        //O horário inicial precisa ser anterior ao horário final.
        if(!dataInicio.isBefore(dataFim)){
            throw new IllegalArgumentException("O horario inicial deve ser anterior ao horario final!");
        }

        //Não é permitido criar uma reserva para uma sala inexistente.
        SalaEntity sala = salaRepository.findById(idSala).orElseThrow(() -> new RecursoNaoEncontrado("Não é possivel reservar, sala não encontrada!"));
        
        //Não é permitido criar uma reserva para um usuario inexistente.
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RecursoNaoEncontrado("Não é possivel reservar, usuario não encontrado!"));

        //Não é permitido criar uma reserva com uma sala interditada/inativa
        if(!sala.isAtivo()){
            throw new RecursoConflitantes("Não é possivel reservar, esta sala esta interditada.");
        }

        //Não podem existir duas reservas ativas para a mesma sala em horários conflitantes.
        if(reservaRepository.existsReservaConflitante(idSala, status, dataInicio, dataFim)){
            throw new RecursoConflitantes("Não é possivel reservar, esse horario já esta ocupado.");
        }

        ReservaEntity reserva = new ReservaEntity();
        reserva.setSala(sala);
        reserva.setUsuario(usuario);
        reserva.setDataInicio(dataInicio);
        reserva.setDataFim(dataFim);
        reserva.setStatus(status);

        ReservaEntity reservaSalva = reservaRepository.save(reserva);

        return toResponseDTO(reservaSalva);
    }

    public List<ReservaResponseDTO> listarReservasDaSala(Long idSala){
        salaRepository.findById(idSala).orElseThrow(() -> new RecursoNaoEncontrado("Sala não encontrada!"));

        List<ReservaEntity> salaComReservas = reservaRepository.findBySalaIdSala(idSala);
        if(salaComReservas.isEmpty()){
            throw new RecursoNaoEncontrado("Reservas não encontradas para essa sala!");

        }

        return salaComReservas.stream()
                               .map(this::toResponseDTO)
                               .toList();

    }

    public List<ReservaResponseDTO> listarReservasDoUsuario(Long idUsuario){
        usuarioRepository.findById(idUsuario).orElseThrow(() -> new RecursoNaoEncontrado("Usuario não encontrada!"));
        
        List<ReservaEntity> usuariosComReservas = reservaRepository.findSalasReservadasPorUsuario(idUsuario);
        if(usuariosComReservas.isEmpty()){
            throw new RecursoNaoEncontrado("Reservas não encontradas para esse usuario!");

        }

        return usuariosComReservas.stream()
                                  .map(this::toResponseDTO)
                                  .toList();
    }

    public List<ReservaResponseDTO> listarReservasPorData(LocalDate data){
        if(data == null){
            throw new IllegalArgumentException("A data para a consulta é obrigatória!");
        }

        Instant inicioDoDia = data.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant fimDoDia = data.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        List<ReservaEntity> reservas = reservaRepository.findReservasPorDia(inicioDoDia, fimDoDia);

        if(reservas.isEmpty()){
            throw new RecursoNaoEncontrado("Nenhuma reserva encontrada para a data informada!");
        }

        return reservas.stream()
                       .map(this::toResponseDTO)
                       .toList();
    }

    public List<ReservaResponseDTO> listarTodas(){
        List<ReservaEntity> reservas = reservaRepository.findAll();
        return reservas.stream()
                    .map(this::toResponseDTO)
                    .toList();
    }

    public List<ReservaResponseDTO> listarReservasPorStatus(StatusReserva status){
        
        List<ReservaEntity> reservasPorStatus = reservaRepository.findByStatus(status);
        
        if(reservasPorStatus.isEmpty()){
            throw new RecursoNaoEncontrado("Reservas não encontradas para esse status!");
        }

        return reservasPorStatus.stream()
                                .map(this::toResponseDTO)
                                .toList();
    }

    public ReservaResponseDTO buscarPorId(Long idReserva){
        ReservaEntity reserva = reservaRepository.findById(idReserva).orElseThrow(() -> new RecursoNaoEncontrado("Reserva não encontrada para esse ID!"));

        return toResponseDTO(reserva);
    }

    @Transactional 
    public ReservaResponseDTO cancelarReserva(Long idReserva){
        ReservaEntity reserva = reservaRepository.findById(idReserva).orElseThrow(() -> new RecursoNaoEncontrado("Reserva não encontrada para esse ID!"));
        
        if (reserva.getStatus() == StatusReserva.CANCELADO){
            throw new RecursoConflitantes("Essa reserva já se encontra cancelada!");
        }

        reserva.setStatus(StatusReserva.CANCELADO);
        ReservaEntity reservaAtualizada = reservaRepository.save(reserva);

        return toResponseDTO(reservaAtualizada);
    }

    public ReservaResponseDTO toResponseDTO(ReservaEntity reserva){
        return new ReservaResponseDTO(
            reserva.getIdReserva(),
            reserva.getSala().getIdSala(),
            reserva.getUsuario().getIdUsuario(),
            reserva.getDataInicio(),
            reserva.getDataFim(),
            reserva.getStatus()
        );
    }
}
