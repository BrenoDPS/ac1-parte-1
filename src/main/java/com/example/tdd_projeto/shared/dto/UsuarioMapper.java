package com.example.tdd_projeto.shared.dto;

import com.example.tdd_projeto.domain.entity.Usuario;
import com.example.tdd_projeto.domain.valueobject.Email;

/**
 * Classe utilitária para converter entre Usuario e UsuarioDTO.
 */
public class UsuarioMapper {
    
    /**
     * Converte Entity para DTO
     */
    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail() != null ? usuario.getEmail().getEndereco() : null)
                .pontuacaoTotal(usuario.getPontuacaoTotal())
                .dataCadastro(usuario.getDataCadastro())
                .ativo(usuario.getAtivo())
                .build();
    }
    
    /**
     * Converte DTO para Entity
     */
    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Usuario.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .email(dto.getEmail() != null ? Email.de(dto.getEmail()) : null)
                .pontuacaoTotal(dto.getPontuacaoTotal())
                .dataCadastro(dto.getDataCadastro())
                .ativo(dto.getAtivo())
                .build();
    }
    
    /**
     * Atualiza uma entidade existente com dados do DTO
     */
    public static void updateEntityFromDTO(Usuario usuario, UsuarioDTO dto) {
        if (dto.getNome() != null) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getEmail() != null) {
            usuario.setEmail(Email.de(dto.getEmail()));
        }
        // pontuacaoTotal e dataCadastro não devem ser alterados manualmente
        if (dto.getAtivo() != null) {
            usuario.setAtivo(dto.getAtivo());
        }
    }
}