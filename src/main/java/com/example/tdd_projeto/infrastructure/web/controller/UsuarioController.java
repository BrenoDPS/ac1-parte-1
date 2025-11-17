package com.example.tdd_projeto.infrastructure.web.controller;

import com.example.tdd_projeto.application.service.UsuarioService;
import com.example.tdd_projeto.shared.dto.UsuarioDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações de Usuário
 */
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO criado = usuarioService.criar(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }
    
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarAtivos() {
        List<UsuarioDTO> usuarios = usuarioService.listarAtivos();
        return ResponseEntity.ok(usuarios);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO atualizado = usuarioService.atualizar(id, usuarioDTO);
        return ResponseEntity.ok(atualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        usuarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/pontos")
    public ResponseEntity<UsuarioDTO> adicionarPontos(
            @PathVariable Long id,
            @RequestParam Integer pontos) {
        UsuarioDTO atualizado = usuarioService.adicionarPontos(id, pontos);
        return ResponseEntity.ok(atualizado);
    }
    
    @GetMapping("/ranking")
    public ResponseEntity<List<UsuarioDTO>> listarTopPorPontuacao() {
        List<UsuarioDTO> ranking = usuarioService.listarTopPorPontuacao();
        return ResponseEntity.ok(ranking);
    }
}