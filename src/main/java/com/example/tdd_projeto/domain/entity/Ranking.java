package com.example.tdd_projeto.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade que representa a posição de um usuário no ranking em um período.
 */
@Entity
@Table(name = "rankings", 
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"usuario_id", "periodo", "data_referencia"}
       ))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "usuario")
public class Ranking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(nullable = false)
    private Integer posicao;
    
    @Column(nullable = false)
    private Integer pontuacao;
    
    @Column(name = "variacao_posicao")
    private Integer variacaoPosicao; // +2, -1, 0, etc.
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PeriodoRanking periodo;
    
    @Column(name = "data_referencia", nullable = false)
    private LocalDate dataReferencia;
    
    @Column(name = "data_calculo", nullable = false, updatable = false)
    private LocalDateTime dataCalculo;
    
    /**
     * Enum para períodos de ranking
     */
    public enum PeriodoRanking {
        DIARIO("Diário"),
        SEMANAL("Semanal"),
        MENSAL("Mensal"),
        ANUAL("Anual"),
        GERAL("Geral");
        
        private final String descricao;
        
        PeriodoRanking(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }
    }
    
    @PrePersist
    protected void onCreate() {
        if (this.dataCalculo == null) {
            this.dataCalculo = LocalDateTime.now();
        }
    }
    
    // Métodos de negócio
    
    /**
     * Retorna variação formatada (+2, -1, =)
     */
    public String getVariacaoFormatada() {
        if (variacaoPosicao == null || variacaoPosicao == 0) {
            return "=";
        }
        return variacaoPosicao > 0 ? "+" + variacaoPosicao : String.valueOf(variacaoPosicao);
    }
    
    /**
     * Verifica se houve melhora na posição
     */
    public boolean melhorou() {
        return variacaoPosicao != null && variacaoPosicao > 0;
    }
}