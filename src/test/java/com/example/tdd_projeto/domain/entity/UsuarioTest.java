package com.example.tdd_projeto.domain.entity;

import com.example.tdd_projeto.domain.valueobject.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testes unitários para a entidade Usuario
 * Testa regras de negócio e validações
 */
@DisplayName("Testes da Entidade Usuario")
class UsuarioTest {
    
    private Usuario usuario;
    
    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .nome("João Silva")
                .email(Email.de("joao@teste.com"))
                .pontuacaoTotal(100)
                .ativo(true)
                .build();
    }
    
    @Test
    @DisplayName("Deve criar usuário com builder")
    void deveCriarUsuarioComBuilder() {
        // WHEN
        Usuario novoUsuario = Usuario.builder()
                .nome("Maria Santos")
                .email(Email.de("maria@teste.com"))
                .build();
        
        // THEN
        assertThat(novoUsuario).isNotNull();
        assertThat(novoUsuario.getNome()).isEqualTo("Maria Santos");
        assertThat(novoUsuario.getEmail().getEndereco()).isEqualTo("maria@teste.com");
    }
    
    @Test
    @DisplayName("Deve adicionar pontos ao usuário")
    void deveAdicionarPontos() {
        // GIVEN
        int pontosIniciais = usuario.getPontuacaoTotal();
        
        // WHEN
        usuario.adicionarPontos(50);
        
        // THEN
        assertThat(usuario.getPontuacaoTotal()).isEqualTo(pontosIniciais + 50);
    }
    
    @Test
    @DisplayName("Não deve adicionar pontos negativos")
    void naoDeveAdicionarPontosNegativos() {
        // WHEN & THEN
        assertThatThrownBy(() -> usuario.adicionarPontos(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pontos devem ser positivos");
    }
    
    @Test
    @DisplayName("Não deve adicionar pontos nulos")
    void naoDeveAdicionarPontosNulos() {
        // WHEN & THEN
        assertThatThrownBy(() -> usuario.adicionarPontos(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pontos devem ser positivos");
    }
    
    @Test
    @DisplayName("Deve desativar usuário")
    void deveDesativarUsuario() {
        // GIVEN
        assertThat(usuario.getAtivo()).isTrue();
        
        // WHEN
        usuario.desativar();
        
        // THEN
        assertThat(usuario.getAtivo()).isFalse();
    }
    
    @Test
    @DisplayName("Deve reativar usuário")
    void deveReativarUsuario() {
        // GIVEN
        usuario.desativar();
        assertThat(usuario.getAtivo()).isFalse();
        
        // WHEN
        usuario.ativar();
        
        // THEN
        assertThat(usuario.getAtivo()).isTrue();
    }
    
    @Test
    @DisplayName("Deve comparar usuários por ID (equals/hashCode)")
    void deveCompararUsuariosPorId() {
        // GIVEN
        Usuario usuario1 = Usuario.builder()
                .id(1L)
                .nome("Usuario 1")
                .email(Email.de("user1@teste.com"))
                .build();
        
        Usuario usuario2 = Usuario.builder()
                .id(1L)
                .nome("Usuario 2") // Nome diferente, mas ID igual
                .email(Email.de("user2@teste.com"))
                .build();
        
        Usuario usuario3 = Usuario.builder()
                .id(2L)
                .nome("Usuario 1")
                .email(Email.de("user1@teste.com"))
                .build();
        
        // THEN
        assertThat(usuario1).isEqualTo(usuario2); // Mesmo ID
        assertThat(usuario1).isNotEqualTo(usuario3); // IDs diferentes
        assertThat(usuario1.hashCode()).isEqualTo(usuario2.hashCode());
    }
}