package org.litethinking.supermercado.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for the web module.
 */
@Configuration
public class WebConfig {

    @Value("${api.base-url}")
    private String apiBaseUrl;

    /**
     * Creates a RestTemplate bean for making HTTP requests to the REST API.
     * 
     * @return the RestTemplate bean
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Gets the base URL of the REST API.
     * 
     * @return the base URL of the REST API
     */
    @Bean
    public String apiBaseUrl() {
        return apiBaseUrl;
    }
}