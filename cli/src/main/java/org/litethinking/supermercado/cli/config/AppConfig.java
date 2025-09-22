package org.litethinking.supermercado.cli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration class for application beans.
 * This class is responsible for creating and configuring beans that can be used throughout the application.
 */
@Configuration
public class AppConfig {
    
    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);
    
    /**
     * Creates a RestTemplate bean for making HTTP requests to the API.
     * 
     * @return a configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        log.debug("Creating RestTemplate bean for API communication");
        return new RestTemplate();
    }
}