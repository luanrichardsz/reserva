package com.reserva.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.reserva.model.ReservaEntity;
import com.reserva.model.SalaEntity;
import com.reserva.model.StatusReserva;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Long>, JpaSpecificationExecutor<ReservaEntity>{
    //buscar as reservas pela sala que esta reservada
    List<ReservaEntity> findBySalaIdSala(Long idSala);
    
    //buscar as reservas pelo Usuario que reservou
    List<ReservaEntity> findByUsuarioIdUsuario(Long idUsuario);
    
    //buscar as reservas de um usuario em uma sala especifica
    List<ReservaEntity> findByUsuarioIdUsuarioAndSalaIdSala(Long idUsuario, Long idSala);
    
    //buscar pelo status da reserva
    List<ReservaEntity> findByStatus(StatusReserva status);
    
    // Validar as reservas com a mesma data e hora
    @Query("""
           SELECT COUNT(r) > 0 FROM ReservaEntity r
           WHERE r.sala.idSala = :idSala
            AND r.status = :status
            AND r.dataInicio < :fim
            AND r.dataFim > :inicio
           """)
    boolean existsReservaConflitante(
        @Param("idSala") Long idSala,
        @Param("status") StatusReserva status,
        @Param("inicio") Instant inicio,
        @Param("fim") Instant fim
    );

    // Reservadas por um usuario especifico
    @Query("SELECT r FROM ReservaEntity r WHERE r.usuario.idUsuario = :idUsuario")
    List<ReservaEntity> findSalasReservadasPorUsuario(@Param("idUsuario") Long idUsuario);

    // Salas que estão reservadas atualmente
    @Query("SELECT DISTINCT r.sala FROM ReservaEntity r WHERE r.status = :status")
    List<SalaEntity> findSalasComReservasAtivas(@Param("status") StatusReserva status);

    // Listar reservas pela data de inicio
    @Query("SELECT r FROM ReservaEntity r WHERE r.dataFim > :diaInicio AND r.dataInicio < :diaFim")
    List<ReservaEntity> findReservasPorDia(@Param("diaInicio") Instant diaInicio, @Param("diaFim") Instant diaFim);
}