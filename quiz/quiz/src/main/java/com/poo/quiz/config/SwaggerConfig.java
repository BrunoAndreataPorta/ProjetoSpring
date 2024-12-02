package com.poo.quiz.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API do QUIZ",
                version = "1.0",
                description = "Esta API gerencia perguntas e partidas geradas."
        )
)

public class SwaggerConfig {
}
