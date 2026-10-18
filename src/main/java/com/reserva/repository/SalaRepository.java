package com.reserva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.reserva.model.SalaEntity;

public interface SalaRepository extends JpaRepository<SalaEntity, Long>, JpaSpecificationExecutor<SalaEntity> {
    //buscar as salas que estão ativas para reservas
    boolean existsSalasByAtivo(boolean ativo);

    //listar as salas que estão ativas para reservas
    List<SalaEntity> findByAtivoTrue();

    //Buscas as salas que tem o mesmo nome
    boolean existsByNome(String nome);
}
