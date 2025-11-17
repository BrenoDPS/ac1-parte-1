package com.example.tdd_projeto.domain.repository;

import com.example.tdd_projeto.domain.entity.Engajamento;
import com.example.tdd_projeto.domain.entity.Engajamento.TipoEngajamento;
import com.example.tdd_projeto.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EngajamentoRepository extends JpaRepository<Engajamento, Long> {
    
    /**
     * Busca engajamentos de um usuário
     */
    List<Engajamento> findByUsuario(Usuario usuario);
    
    /**
     * Busca engajamentos por tipo
     */
    List<Engajamento> findByTipo(TipoEngajamento tipo);
    
    /**
     * Busca engajamentos em um período
     */
    List<Engajamento> findByDataEngajamentoBetween(LocalDateTime inicio, LocalDateTime fim);
    
    /**
     * Calcula total de pontos de um usuário
     */
    @Query("SELECT COALESCE(SUM(e.pontos), 0) FROM Engajamento e WHERE e.usuario = :usuario")
    Integer calcularTotalPontosUsuario(@Param("usuario") Usuario usuario);
}