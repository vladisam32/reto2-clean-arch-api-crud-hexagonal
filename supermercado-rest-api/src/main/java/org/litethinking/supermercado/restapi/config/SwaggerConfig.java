package org.litethinking.supermercado.restapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.List;

/**
 * Configuration class for Swagger/OpenAPI with controller advisor suppression.
 * This configuration helps resolve compatibility issues between Spring's ControllerAdviceBean
 * and Swagger documentation generation.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates a GroupedOpenApi bean that excludes controller advisors.
     * This helps prevent NoSuchMethodError related to ControllerAdviceBean initialization.
     *
     * @return GroupedOpenApi bean configured to exclude controller advisors
     */
    @Bean
    @Primary
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("api")
                // Only include paths that start with /api to avoid processing error handlers
                .pathsToMatch("/api/**", "/v1/**", "/v2/**", "/v3/**")
                // Explicitly exclude controller advisors and error handlers
                .addOpenApiCustomizer(openApi -> {
                    // Apply the same customization as in OpenApiConfig
                    openApi.info(createApiInfo());
                    openApi.servers(createApiServers());
                })
                // Explicitly exclude controller advisors by only scanning controller package
                .packagesToScan("org.litethinking.supermercado.restapi.controller")
                .build();
    }

    /**
     * Creates the OpenAPI bean with custom configuration.
     * This is marked with @Profile("swagger-custom") to ensure it's only active
     * when the swagger-custom profile is active.
     *
     * @return Customized OpenAPI object
     */
    @Bean
    @Profile("swagger-custom")
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(createApiInfo())
                .servers(createApiServers());
    }

    /**
     * Creates the API info object with title, description, etc.
     *
     * @return Info object for OpenAPI
     */
    private Info createApiInfo() {
        return new Info()
                .title("API REST MANIN pa' Gestión del Supermercado")
                .version("1.0.0")
                .description("API REST pa' manejar to' lo del supermercado, con sus productos, inventario, ventas, cajeros y clientes, ¡tú 'ta claro!")
                .contact(new Contact()
                        .name("El Equipazo de Desarrollo")
                        .email("VLADISAM32@GMAIL.COM"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }

    /**
     * Creates the API servers list.
     *
     * @return List of Server objects for OpenAPI
     */
    private List<Server> createApiServers() {
        return List.of(new Server()
                .url("http://localhost:8080")
                .description("Servidor local pa' desarrollar, ¡dale!"));
    }
}
