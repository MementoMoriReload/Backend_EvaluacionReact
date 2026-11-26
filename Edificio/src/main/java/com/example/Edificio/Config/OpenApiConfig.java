package com.example.Edificio.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI api() {
        return new OpenAPI().info(new Info()
                .title("Acceso Seguro - Edificio")
                .description("Control de entradas y salidas por edificio")
                .version("1.0"));
    }
}