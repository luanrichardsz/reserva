package com.reserva.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reserva.dto.UsuarioRequestDTO;
import com.reserva.dto.UsuarioResponseDTO;
import com.reserva.model.UsuarioEntity;
import com.reserva.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;

    @Transactional 
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto){
        String nome = dto.nome();
        String cargo = dto.cargo();
        String setor = dto.setor();
        
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(nome);
        usuario.setCargo(cargo);
        usuario.setSetor(setor);

        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuario);

        return toResponseDTO(usuarioSalvo);
    }

    public List<UsuarioResponseDTO> listarTodos(){
        List<UsuarioEntity> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                       .map(this::toResponseDTO)
                       .toList();
    }

    private UsuarioResponseDTO toResponseDTO(UsuarioEntity usuario){
        return new UsuarioResponseDTO(    
            usuario.getIdUsuario(), 
            usuario.getNome(), 
            usuario.getCargo(), 
            usuario.getSetor()
        );
    }
}
