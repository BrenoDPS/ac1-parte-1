package com.example.tdd_projeto.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um Conteúdo publicado por um usuário.
 */
@Entity
@Table(name = "conteudos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"autor", "engajamentos"})
public class Conteudo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;
    
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 5, max = 200, message = "Título deve ter entre 5 e 200 caracteres")
    @Column(nullable = false, length = 200)
    private String titulo;
    
    @NotBlank(message = "Texto é obrigatório")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoConteudo tipo;
    
    @Column(name = "data_publicacao", nullable = false, updatable = false)
    private LocalDateTime dataPublicacao;
    
    @Column(name = "visualizacoes")
    @Builder.Default
    private Integer visualizacoes = 0;
    
    @OneToMany(mappedBy = "conteudo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Engajamento> engajamentos = new ArrayList<>();
    
    /**
     * Enum para tipos de conteúdo
     */
    public enum TipoConteudo {
        ARTIGO,
        PERGUNTA,
        RESPOSTA,
        TUTORIAL,
        DISCUSSAO
    }
    
    @PrePersist
    protected void onCreate() {
        if (this.dataPublicacao == null) {
            this.dataPublicacao = LocalDateTime.now();
        }
        if (this.visualizacoes == null) {
            this.visualizacoes = 0;
        }
    }
    
    // Métodos de negócio
    
    /**
     * Incrementa o contador de visualizações
     */
    public void visualizar() {
        this.visualizacoes++;
    }
    
    /**
     * Retorna total de engajamentos
     */
    public int getTotalEngajamentos() {
        return engajamentos.size();
    }
    
    /**
     * Adiciona um engajamento ao conteúdo
     */
    public void adicionarEngajamento(Engajamento engajamento) {
        engajamentos.add(engajamento);
        engajamento.setConteudo(this);
    }
}