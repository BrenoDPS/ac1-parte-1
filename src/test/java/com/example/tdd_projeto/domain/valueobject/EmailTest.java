package com.example.tdd_projeto.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testes para o Value Object Email
 * Garante validação e imutabilidade
 */
@DisplayName("Testes do Value Object Email")
class EmailTest {
    
    @Test
    @DisplayName("Deve criar email válido")
    void deveCriarEmailValido() {
        // WHEN
        Email email = Email.de("usuario@exemplo.com");
        
        // THEN
        assertThat(email).isNotNull();
        assertThat(email.getEndereco()).isEqualTo("usuario@exemplo.com");
    }
    
    @ParameterizedTest
    @DisplayName("Deve aceitar emails válidos em diferentes formatos")
    @ValueSource(strings = {
        "user@domain.com",
        "user.name@domain.com",
        "user+tag@domain.co.uk",
        "user123@test-domain.com",
        "123@domain.com"
    })
    void deveAceitarEmailsValidos(String emailValido) {
        // WHEN
        Email email = Email.de(emailValido);
        
        // THEN
        assertThat(email.getEndereco()).isEqualTo(emailValido);
    }
    
    @ParameterizedTest
    @DisplayName("Deve rejeitar emails inválidos")
    @ValueSource(strings = {
        "invalid",
        "@domain.com",
        "user@",
        "user @domain.com",
        "user@domain",
        ""
    })
    void deveRejeitarEmailsInvalidos(String emailInvalido) {
        // WHEN & THEN
        assertThatThrownBy(() -> Email.de(emailInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email inválido");
    }
    
    @Test
    @DisplayName("Deve rejeitar email nulo")
    void deveRejeitarEmailNulo() {
        // WHEN & THEN
        assertThatThrownBy(() -> Email.de(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email não pode ser vazio");
    }
    
    @Test
    @DisplayName("Deve rejeitar email vazio")
    void deveRejeitarEmailVazio() {
        // WHEN & THEN
        assertThatThrownBy(() -> Email.de("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email não pode ser vazio");
    }
    
    @Test
    @DisplayName("Emails iguais devem ser considerados iguais (equals/hashCode)")
    void emailsIguaisDevemSerIguais() {
        // GIVEN
        Email email1 = Email.de("teste@domain.com");
        Email email2 = Email.de("teste@domain.com");
        
        // THEN
        assertThat(email1).isEqualTo(email2);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
    }
    
    @Test
    @DisplayName("Emails diferentes devem ser considerados diferentes")
    void emailsDiferentesDevemSerDiferentes() {
        // GIVEN
        Email email1 = Email.de("user1@domain.com");
        Email email2 = Email.de("user2@domain.com");
        
        // THEN
        assertThat(email1).isNotEqualTo(email2);
    }
}