package io.loghub.loghub_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Permitir origens do frontend (desenvolvimento e produção)
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",  // Vite dev server
            "http://localhost:3000",  // Create React App
            "http://127.0.0.1:5173",
            "http://127.0.0.1:3000"
        ));

        // Permitir todos os métodos HTTP
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Permitir todos os headers
        config.setAllowedHeaders(List.of("*"));

        // Permitir credenciais (cookies, authorization headers)
        config.setAllowCredentials(true);

        // Expor headers que o frontend pode acessar
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Cache da configuração preflight por 1 hora
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}

