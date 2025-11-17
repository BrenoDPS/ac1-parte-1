package com.example.tdd_projeto.domain.repository;

import com.example.tdd_projeto.domain.entity.Conteudo;
import com.example.tdd_projeto.domain.entity.Conteudo.TipoConteudo;
import com.example.tdd_projeto.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConteudoRepository extends JpaRepository<Conteudo, Long> {
    
    /**
     * Busca conteúdos de um autor específico
     */
    List<Conteudo> findByAutor(Usuario autor);
    
    /**
     * Busca conteúdos por tipo
     */
    List<Conteudo> findByTipo(TipoConteudo tipo);
    
    /**
     * Busca conteúdos mais visualizados
     */
    @Query("SELECT c FROM Conteudo c ORDER BY c.visualizacoes DESC")
    List<Conteudo> findTopConteudosByVisualizacoes();
    
    /**
     * Conta quantos conteúdos um autor publicou
     */
    Long countByAutor(Usuario autor);
}