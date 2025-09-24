package org.litethinking.supermercado.restapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.litethinking.supermercado.restapi.config.DataInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main class for the REST API application.
 */
@SpringBootApplication
@Import(DataInitializer.class)
@ComponentScan(basePackages = {
    "org.litethinking.supermercado",
/*    "org.litethinking.application.service.supermercado.impl",
    "org.litethinking.application.service.supermercado.inventario",
    "org.litethinking.application.service.supermercado.inventario.impl",*/
/*    "org.litethinking.restapi.controller.supermercado",
    "org.litethinking.restapi.controller.supermercado.inventario",
    "org.litethinking.restapi.config",
    "org.litethinking.domain.model.supermercado",
    "org.litethinking.domain.model.supermercado.inventario",
    "org.litethinking.infrastructure.persistence.adapter.supermercado",
    "org.litethinking.infrastructure.persistence.adapter.supermercado.inventario"*/
})
@EntityScan(basePackages = {
        "org.litethinking.supermercado.infrastructure.entity"
    /*"org.litethinking.infrastructure.persistence.entity.supermercado",
    "org.litethinking.infrastructure.persistence.entity.supermercado.inventario"*/
})
@EnableJpaRepositories(basePackages = {
    "org.litethinking.supermercado.infrastructure.persistence.repository"
    /*"org.litethinking.infrastructure.persistence.repository.supermercado",
    "org.litethinking.infrastructure.persistence.repository.supermercado.inventario",
    "org.litethinking.infrastructure.persistence.repository.supermercado.venta"*/
})
public class RestApiApplication {

    private static final Logger logger = LogManager.getLogger(RestApiApplication.class);

    public static void main(String[] args) {
        logger.info("Starting REST API Application");
        try {
            ConfigurableApplicationContext context = SpringApplication.run(RestApiApplication.class, args);
            logger.info("REST API Application started successfully");
        } catch (Exception e) {
            logger.error("Error starting REST API Application", e);
            throw e;
        }
    }
}
