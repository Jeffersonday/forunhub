package com.exemple.forumhub.springdoc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sound.sampled.Line;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer").bearerFormat("JWT")))
                .info(new Line.Info
                        .title("Fórum API")
                        .description("API Rest de uma aplicação de um fórum, contendo " +
                                "as funcionalidades de CRUD de tópicos e autenticação com token JWT"));
    }


}