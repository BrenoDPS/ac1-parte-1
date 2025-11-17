package com.example.tdd_projeto.domain.repository;

import com.example.tdd_projeto.domain.entity.Ranking;
import com.example.tdd_projeto.domain.entity.Ranking.PeriodoRanking;
import com.example.tdd_projeto.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    
    /**
     * Busca rankings de um período ordenados por posição
     */
    List<Ranking> findByPeriodoOrderByPosicaoAsc(PeriodoRanking periodo);
    
    /**
     * Busca ranking específico de um usuário em um período
     */
    Optional<Ranking> findByUsuarioAndPeriodoAndDataReferencia(
        Usuario usuario, 
        PeriodoRanking periodo, 
        LocalDate dataReferencia
    );
    
    /**
     * Busca todos os rankings de um usuário
     */
    List<Ranking> findByUsuarioOrderByDataCalculoDesc(Usuario usuario);
}