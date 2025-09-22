package org.litethinking.supermercado.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuration class for JPA repositories in the infrastructure module.
 * This enables Spring Data JPA repositories in all repository packages.
 */
/*@Configuration
@EnableJpaRepositories(basePackages = {
    "org.litethinking.supermercado.infrastructure"
})*/
public class PersistenceConfig {
    // Configuration is handled by annotations
}