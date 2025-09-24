package org.litethinking.supermercado.restapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    @Profile("!swagger-custom")
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST MANIN pa' Gestión del Supermercado")
                        .version("1.0.0")
                        .description("API REST pa' manejar to' lo del supermercado, con sus productos, inventario, ventas, cajeros y clientes, ¡tú 'ta claro!")
                        .contact(new Contact()
                                .name("El Equipazo de Desarrollo")
                                .email("VLADISAM32@GMAIL.COM"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor local pa' desarrollar, ¡dale!")));
    }
}
