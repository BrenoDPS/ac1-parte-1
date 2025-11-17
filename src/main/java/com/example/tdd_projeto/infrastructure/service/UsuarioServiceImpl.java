package com.example.tdd_projeto.infrastructure.service;

import com.example.tdd_projeto.application.service.UsuarioService;
import com.example.tdd_projeto.domain.entity.Usuario;
import com.example.tdd_projeto.domain.repository.UsuarioRepository;
import com.example.tdd_projeto.shared.dto.UsuarioDTO;
import com.example.tdd_projeto.shared.dto.UsuarioMapper;
import com.example.tdd_projeto.shared.exception.BusinessException;
import com.example.tdd_projeto.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Usuário
 */
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    @Override
    public UsuarioDTO criar(UsuarioDTO usuarioDTO) {
        // Validação: email único
        if (usuarioDTO.getEmail() != null) {
            usuarioRepository.findByEmail_Endereco(usuarioDTO.getEmail())
                .ifPresent(u -> {
                    throw new BusinessException("EMAIL_JA_EXISTE", 
                        "Email " + usuarioDTO.getEmail() + " já está cadastrado");
                });
        }
        
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return UsuarioMapper.toDTO(usuarioSalvo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return UsuarioMapper.toDTO(usuario);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarAtivos() {
        return usuarioRepository.findByAtivoTrue()
            .stream()
            .map(UsuarioMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        
        // Validação: se mudou email, verificar se novo email já existe
        if (usuarioDTO.getEmail() != null && 
            !usuarioDTO.getEmail().equals(usuario.getEmail().getEndereco())) {
            usuarioRepository.findByEmail_Endereco(usuarioDTO.getEmail())
                .ifPresent(u -> {
                    throw new BusinessException("EMAIL_JA_EXISTE", 
                        "Email " + usuarioDTO.getEmail() + " já está cadastrado");
                });
        }
        
        UsuarioMapper.updateEntityFromDTO(usuario, usuarioDTO);
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return UsuarioMapper.toDTO(usuarioAtualizado);
    }
    
    @Override
    public void desativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        usuario.desativar();
        usuarioRepository.save(usuario);
    }
    
    @Override
    public UsuarioDTO adicionarPontos(Long id, Integer pontos) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        usuario.adicionarPontos(pontos);
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return UsuarioMapper.toDTO(usuarioAtualizado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTopPorPontuacao() {
        return usuarioRepository.findTopUsuariosByPontuacao()
            .stream()
            .map(UsuarioMapper::toDTO)
            .collect(Collectors.toList());
    }
}