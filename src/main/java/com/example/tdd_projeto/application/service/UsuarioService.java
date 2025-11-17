package com.example.tdd_projeto.application.service;

import com.example.tdd_projeto.shared.dto.UsuarioDTO;

import java.util.List;

/**
 * Interface de serviço para operações de Usuário
 * Seguindo Clean Architecture: interface no core, implementação na infra
 */
public interface UsuarioService {
    
    /**
     * Cria um novo usuário
     */
    UsuarioDTO criar(UsuarioDTO usuarioDTO);
    
    /**
     * Busca usuário por ID
     */
    UsuarioDTO buscarPorId(Long id);
    
    /**
     * Lista todos os usuários ativos
     */
    List<UsuarioDTO> listarAtivos();
    
    /**
     * Atualiza um usuário existente
     */
    UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO);
    
    /**
     * Desativa um usuário (soft delete)
     */
    void desativar(Long id);
    
    /**
     * Adiciona pontos a um usuário
     */
    UsuarioDTO adicionarPontos(Long id, Integer pontos);
    
    /**
     * Lista top usuários por pontuação
     */
    List<UsuarioDTO> listarTopPorPontuacao();
}