package com.agitex.climax.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuration de Open API .
 */
@Configuration
public class OpenApiConfig {

    /**
     * Bean de configuration d'Open API.
     *
     * @return une instance OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("CLIMAX API")
                        .description("Documentation des APIs Restful.")
                        .version("1.0.0"));
    }
}
