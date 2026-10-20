package com.example.apiBase.apiBancoBase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/livros/**") // Permite todas as rotas requisitadas para a API
                        .allowedOrigins("http://localhost:3000") // Permite chamadas para esse endereço
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos

                        .allowCredentials(true); // Permite envio de cookies (se necessário)
            }

        };

}
}