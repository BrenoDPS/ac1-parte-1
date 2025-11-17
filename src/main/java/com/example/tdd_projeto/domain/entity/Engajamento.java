package com.example.tdd_projeto.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma interação de um usuário com um conteúdo.
 */
@Entity
@Table(name = "engajamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"usuario", "conteudo"})
public class Engajamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conteudo_id")
    private Conteudo conteudo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoEngajamento tipo;
    
    @Column(nullable = false)
    private Integer pontos;
    
    @Column(name = "data_engajamento", nullable = false, updatable = false)
    private LocalDateTime dataEngajamento;
    
    /**
     * Enum para tipos de engajamento e pontuação associada
     */
    public enum TipoEngajamento {
        POSTAGEM(50),
        RESPOSTA(30),
        CURTIDA(10),
        COMENTARIO(20),
        COMPARTILHAMENTO(15);
        
        private final int pontos;
        
        TipoEngajamento(int pontos) {
            this.pontos = pontos;
        }
        
        public int getPontos() {
            return pontos;
        }
    }
    
    @PrePersist
    protected void onCreate() {
        if (this.dataEngajamento == null) {
            this.dataEngajamento = LocalDateTime.now();
        }
        if (this.pontos == null && this.tipo != null) {
            this.pontos = this.tipo.getPontos();
        }
    }
    
    // Método de negócio
    
    /**
     * Define o tipo de engajamento e calcula pontos automaticamente
     */
    public void definirTipo(TipoEngajamento tipo) {
        this.tipo = tipo;
        this.pontos = tipo.getPontos();
    }
}