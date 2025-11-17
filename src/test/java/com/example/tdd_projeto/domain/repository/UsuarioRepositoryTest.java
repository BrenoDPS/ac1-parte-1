package com.example.tdd_projeto.domain.repository;

import com.example.tdd_projeto.domain.entity.Usuario;
import com.example.tdd_projeto.domain.valueobject.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para UsuarioRepository usando @DataJpaTest
 * 
 * @DataJpaTest configura um banco H2 em memória apenas para testes
 * TestEntityManager permite manipular entidades nos testes
 */
@DataJpaTest
@DisplayName("Testes do UsuarioRepository")
class UsuarioRepositoryTest {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    private Usuario usuarioTeste;
    
    /**
     * Executado ANTES de cada teste
     * Prepara dados de teste
     */
    @BeforeEach
    void setUp() {
        // Limpa o banco antes de cada teste
        usuarioRepository.deleteAll();
        
        // Cria um usuário de teste
        usuarioTeste = Usuario.builder()
                .nome("João Silva")
                .email(Email.de("joao@teste.com"))
                .pontuacaoTotal(100)
                .ativo(true)
                .build();
    }
    
    @Test
    @DisplayName("Deve salvar um usuário no banco de dados")
    void deveSalvarUsuario() {
        // GIVEN (Dado) - Preparação
        // usuarioTeste já foi criado no @BeforeEach
        
        // WHEN (Quando) - Ação
        Usuario usuarioSalvo = usuarioRepository.save(usuarioTeste);
        
        // THEN (Então) - Verificação
        assertThat(usuarioSalvo).isNotNull();
        assertThat(usuarioSalvo.getId()).isNotNull();
        assertThat(usuarioSalvo.getNome()).isEqualTo("João Silva");
        assertThat(usuarioSalvo.getEmail().getEndereco()).isEqualTo("joao@teste.com");
    }
    
    @Test
    @DisplayName("Deve buscar usuário por email")
    void deveBuscarUsuarioPorEmail() {
        // GIVEN
        entityManager.persistAndFlush(usuarioTeste);
        
        // WHEN
        Optional<Usuario> resultado = usuarioRepository.findByEmail_Endereco("joao@teste.com");
        
        // THEN
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("João Silva");
    }
    
    @Test
    @DisplayName("Deve retornar vazio quando email não existe")
    void deveRetornarVazioQuandoEmailNaoExiste() {
        // WHEN
        Optional<Usuario> resultado = usuarioRepository.findByEmail_Endereco("naoexiste@teste.com");
        
        // THEN
        assertThat(resultado).isEmpty();
    }
    
    @Test
    @DisplayName("Deve listar apenas usuários ativos")
    void deveListarApenasUsuariosAtivos() {
        // GIVEN - Criar 2 usuários ativos e 1 inativo
        Usuario usuarioAtivo1 = Usuario.builder()
                .nome("Maria Santos")
                .email(Email.de("maria@teste.com"))
                .ativo(true)
                .build();
        
        Usuario usuarioAtivo2 = Usuario.builder()
                .nome("Pedro Oliveira")
                .email(Email.de("pedro@teste.com"))
                .ativo(true)
                .build();
        
        Usuario usuarioInativo = Usuario.builder()
                .nome("Ana Costa")
                .email(Email.de("ana@teste.com"))
                .ativo(false)
                .build();
        
        entityManager.persist(usuarioAtivo1);
        entityManager.persist(usuarioAtivo2);
        entityManager.persist(usuarioInativo);
        entityManager.flush();
        
        // WHEN
        List<Usuario> usuariosAtivos = usuarioRepository.findByAtivoTrue();
        
        // THEN
        assertThat(usuariosAtivos).hasSize(2);
        assertThat(usuariosAtivos).extracting(Usuario::getNome)
                .containsExactlyInAnyOrder("Maria Santos", "Pedro Oliveira");
    }
    
    @Test
    @DisplayName("Deve ordenar usuários por pontuação decrescente")
    void deveOrdenarUsuariosPorPontuacao() {
        // GIVEN
        Usuario usuario1 = Usuario.builder()
                .nome("Usuario 1")
                .email(Email.de("user1@teste.com"))
                .pontuacaoTotal(50)
                .ativo(true)
                .build();
        
        Usuario usuario2 = Usuario.builder()
                .nome("Usuario 2")
                .email(Email.de("user2@teste.com"))
                .pontuacaoTotal(200)
                .ativo(true)
                .build();
        
        Usuario usuario3 = Usuario.builder()
                .nome("Usuario 3")
                .email(Email.de("user3@teste.com"))
                .pontuacaoTotal(150)
                .ativo(true)
                .build();
        
        entityManager.persist(usuario1);
        entityManager.persist(usuario2);
        entityManager.persist(usuario3);
        entityManager.flush();
        
        // WHEN
        List<Usuario> ranking = usuarioRepository.findTopUsuariosByPontuacao();
        
        // THEN
        assertThat(ranking).hasSize(3);
        assertThat(ranking.get(0).getNome()).isEqualTo("Usuario 2"); // 200 pontos
        assertThat(ranking.get(1).getNome()).isEqualTo("Usuario 3"); // 150 pontos
        assertThat(ranking.get(2).getNome()).isEqualTo("Usuario 1"); // 50 pontos
    }
    
    @Test
    @DisplayName("Deve contar usuários com pontuação maior que valor")
    void deveContarUsuariosComPontuacaoMaior() {
        // GIVEN
        Usuario usuario1 = Usuario.builder()
                .nome("Usuario 1")
                .email(Email.de("user1@teste.com"))
                .pontuacaoTotal(50)
                .build();
        
        Usuario usuario2 = Usuario.builder()
                .nome("Usuario 2")
                .email(Email.de("user2@teste.com"))
                .pontuacaoTotal(150)
                .build();
        
        Usuario usuario3 = Usuario.builder()
                .nome("Usuario 3")
                .email(Email.de("user3@teste.com"))
                .pontuacaoTotal(200)
                .build();
        
        entityManager.persist(usuario1);
        entityManager.persist(usuario2);
        entityManager.persist(usuario3);
        entityManager.flush();
        
        // WHEN
        Long count = usuarioRepository.countByPontuacaoTotalGreaterThan(100);
        
        // THEN
        assertThat(count).isEqualTo(2); // usuario2 e usuario3
    }
    
    @Test
    @DisplayName("Deve deletar usuário existente")
    void deveDeletarUsuario() {
        // GIVEN
        Usuario usuarioSalvo = entityManager.persistAndFlush(usuarioTeste);
        Long id = usuarioSalvo.getId();
        
        // WHEN
        usuarioRepository.deleteById(id);
        
        // THEN
        Optional<Usuario> resultado = usuarioRepository.findById(id);
        assertThat(resultado).isEmpty();
    }
    
    @Test
    @DisplayName("Deve atualizar pontuação do usuário")
    void deveAtualizarPontuacaoUsuario() {
        // GIVEN
        Usuario usuarioSalvo = entityManager.persistAndFlush(usuarioTeste);
        
        // WHEN
        usuarioSalvo.adicionarPontos(50);
        Usuario usuarioAtualizado = usuarioRepository.save(usuarioSalvo);
        
        // THEN
        assertThat(usuarioAtualizado.getPontuacaoTotal()).isEqualTo(150);
    }
}