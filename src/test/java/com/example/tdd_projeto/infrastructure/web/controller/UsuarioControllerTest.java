package com.example.tdd_projeto.infrastructure.web.controller;

import com.example.tdd_projeto.application.service.UsuarioService;
import com.example.tdd_projeto.shared.dto.UsuarioDTO;
import com.example.tdd_projeto.shared.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes do UsuarioController usando MockMvc
 */
@WebMvcTest(UsuarioController.class)
@DisplayName("Testes do UsuarioController")
class UsuarioControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private UsuarioService usuarioService;
    
    private UsuarioDTO usuarioDTO;
    
    @BeforeEach
    void setUp() {
        usuarioDTO = UsuarioDTO.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@teste.com")
                .pontuacaoTotal(100)
                .ativo(true)
                .build();
    }
    
    @Test
    @DisplayName("POST /api/v1/usuarios - Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() throws Exception {
        // GIVEN
        when(usuarioService.criar(any(UsuarioDTO.class)))
            .thenReturn(usuarioDTO);
        
        // WHEN & THEN
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nome").value("João Silva"))
            .andExpect(jsonPath("$.email").value("joao@teste.com"));
        
        verify(usuarioService, times(1)).criar(any(UsuarioDTO.class));
    }
    
    @Test
    @DisplayName("POST /api/v1/usuarios - Deve retornar 400 para dados inválidos")
    void deveRetornar400ParaDadosInvalidos() throws Exception {
        // GIVEN
        UsuarioDTO usuarioInvalido = UsuarioDTO.builder()
                .nome("Jo") // Nome muito curto
                .email("email-invalido")
                .build();
        
        // WHEN & THEN
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioInvalido)))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("GET /api/v1/usuarios/{id} - Deve buscar usuário por ID")
    void deveBuscarUsuarioPorId() throws Exception {
        // GIVEN
        when(usuarioService.buscarPorId(1L))
            .thenReturn(usuarioDTO);
        
        // WHEN & THEN
        mockMvc.perform(get("/api/v1/usuarios/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nome").value("João Silva"));
    }
    
    @Test
    @DisplayName("GET /api/v1/usuarios/{id} - Deve retornar 404 para usuário inexistente")
    void deveRetornar404ParaUsuarioInexistente() throws Exception {
        // GIVEN
        when(usuarioService.buscarPorId(999L))
            .thenThrow(new ResourceNotFoundException("Usuario", 999L));
        
        // WHEN & THEN
        mockMvc.perform(get("/api/v1/usuarios/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(containsString("não encontrado")));
    }
    
    @Test
    @DisplayName("GET /api/v1/usuarios - Deve listar usuários ativos")
    void deveListarUsuariosAtivos() throws Exception {
        // GIVEN
        UsuarioDTO usuario2 = UsuarioDTO.builder()
                .id(2L)
                .nome("Maria Santos")
                .email("maria@teste.com")
                .build();
        
        List<UsuarioDTO> usuarios = Arrays.asList(usuarioDTO, usuario2);
        when(usuarioService.listarAtivos())
            .thenReturn(usuarios);
        
        // WHEN & THEN
        mockMvc.perform(get("/api/v1/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].nome").value("João Silva"))
            .andExpect(jsonPath("$[1].nome").value("Maria Santos"));
    }
    
    @Test
    @DisplayName("PUT /api/v1/usuarios/{id} - Deve atualizar usuário")
    void deveAtualizarUsuario() throws Exception {
        // GIVEN
        UsuarioDTO usuarioAtualizado = UsuarioDTO.builder()
                .id(1L)
                .nome("João Silva Atualizado")
                .email("joao@teste.com")
                .build();
        
        when(usuarioService.atualizar(anyLong(), any(UsuarioDTO.class)))
            .thenReturn(usuarioAtualizado);
        
        // WHEN & THEN
        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioAtualizado)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("João Silva Atualizado"));
    }
    
    @Test
    @DisplayName("DELETE /api/v1/usuarios/{id} - Deve desativar usuário")
    void deveDesativarUsuario() throws Exception {
        // GIVEN
        doNothing().when(usuarioService).desativar(1L);
        
        // WHEN & THEN
        mockMvc.perform(delete("/api/v1/usuarios/1"))
            .andExpect(status().isNoContent());
        
        verify(usuarioService, times(1)).desativar(1L);
    }
    
    @Test
    @DisplayName("POST /api/v1/usuarios/{id}/pontos - Deve adicionar pontos")
    void deveAdicionarPontos() throws Exception {
        // GIVEN
        UsuarioDTO usuarioComPontos = UsuarioDTO.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@teste.com")
                .pontuacaoTotal(150)
                .build();
        
        when(usuarioService.adicionarPontos(1L, 50))
            .thenReturn(usuarioComPontos);
        
        // WHEN & THEN
        mockMvc.perform(post("/api/v1/usuarios/1/pontos")
                .param("pontos", "50"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pontuacaoTotal").value(150));
    }
    
    @Test
    @DisplayName("GET /api/v1/usuarios/ranking - Deve listar ranking")
    void deveListarRanking() throws Exception {
        // GIVEN
        UsuarioDTO usuario1 = UsuarioDTO.builder()
                .nome("Usuario 1")
                .pontuacaoTotal(200)
                .build();
        
        UsuarioDTO usuario2 = UsuarioDTO.builder()
                .nome("Usuario 2")
                .pontuacaoTotal(150)
                .build();
        
        when(usuarioService.listarTopPorPontuacao())
            .thenReturn(Arrays.asList(usuario1, usuario2));
        
        // WHEN & THEN
        mockMvc.perform(get("/api/v1/usuarios/ranking"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].pontuacaoTotal").value(200));
    }
}