package com.reserva.service;

import java.util.List;

import com.reserva.dto.SalaRequestDTO;
import com.reserva.dto.SalaResponseDTO;
import com.reserva.exception.SalaExistente;
import com.reserva.model.SalaEntity;
import com.reserva.repository.SalaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
public class SalaService {

    private final SalaRepository salaRepository;

    @Transactional 
    public SalaResponseDTO cadastrar(SalaRequestDTO dto){
        String nome = dto.nome();

        if (salaRepository.existsByNome(nome.trim().toLowerCase())) {
            throw new SalaExistente("Esta sala já existe!");
        }

        String descricao = dto.descricao();
        int capacidade = dto.capacidade();
        boolean ativo = true;
        
        SalaEntity sala = new SalaEntity();
        sala.setNome(nome);
        sala.setDescricao(descricao);
        sala.setCapacidade(capacidade);
        sala.setAtivo(ativo);

        SalaEntity salaSalva = salaRepository.save(sala);

        return toResponseDTO(salaSalva);
    }

    public List<SalaResponseDTO> listarTodos(){
        List<SalaEntity> salas = salaRepository.findAll();
        return salas.stream()
                    .map(this::toResponseDTO)
                    .toList();
    }

    public SalaResponseDTO toResponseDTO(SalaEntity sala){
        return new SalaResponseDTO(
            sala.getIdSala(),
            sala.getNome(),
            sala.getDescricao(),
            sala.getCapacidade(),
            sala.isAtivo()
        );
    }
}
