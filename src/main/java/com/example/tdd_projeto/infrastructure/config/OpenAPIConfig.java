package com.example.tdd_projeto.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração simplificada do Swagger/OpenAPI
 */
@Configuration
public class OpenAPIConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("TDD Projeto - API REST")
                .version("1.0.0")
                .description("API para gerenciamento de usuários")
            );
    }
}