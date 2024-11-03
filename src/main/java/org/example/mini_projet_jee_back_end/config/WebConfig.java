package org.example.mini_projet_jee_back_end.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Autorise tous les endpoints
                .allowedOrigins("http://localhost:3000") // Autorise l'origine spécifique
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Autorise les méthodes nécessaires
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

