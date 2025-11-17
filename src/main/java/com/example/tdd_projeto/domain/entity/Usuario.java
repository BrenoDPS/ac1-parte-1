package com.example.tdd_projeto.domain.entity;

import com.example.tdd_projeto.domain.valueobject.Email;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;

/**
 * Entidade de domínio que representa um Usuário do sistema.
 * Segue princípios DDD com validações de negócio.
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor // JPA exige construtor vazio
@AllArgsConstructor
@Builder // Facilita criação: Usuario.builder().nome("João").build()
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Comparar apenas por ID
@ToString(exclude = {"senha", "conteudos", "engajamentos", "rankings"}) // Não exibir senha em logs
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Incluir na comparação
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Embedded // Email é um Value Object embutido
    @AttributeOverride(name = "endereco", column = @Column(name = "email", unique = true, nullable = false))
    private Email email;
    
    @Column(name = "pontuacao_total")
    private Integer pontuacaoTotal = 0;
    
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;
    
    @Column(name = "ativo")
    private Boolean ativo = true;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Conteudo> conteudos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Engajamento> engajamentos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Ranking> rankings = new ArrayList<>();
    
    /**
     * Callback JPA executado antes de persistir pela primeira vez
     */
    @PrePersist
    protected void onCreate() {
        if (this.dataCadastro == null) {
            this.dataCadastro = LocalDateTime.now();
        }
        if (this.ativo == null) {
            this.ativo = true;
        }
        if (this.pontuacaoTotal == null) {
            this.pontuacaoTotal = 0;
        }
    }
    
    // Métodos de negócio (regras do domínio)
    
    /**
     * Adiciona pontos ao usuário
     */
    public void adicionarPontos(Integer pontos) {
        if (pontos == null || pontos < 0) {
            throw new IllegalArgumentException("Pontos devem ser positivos");
        }
        this.pontuacaoTotal += pontos;
    }
    
    /**
     * Desativa o usuário
     */
    public void desativar() {
        this.ativo = false;
    }
    
    /**
     * Reativa o usuário
     */
    public void ativar() {
        this.ativo = true;
    }
}