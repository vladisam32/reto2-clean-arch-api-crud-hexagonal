package org.litethinking.supermercado.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * Main class for the Web application with Thymeleaf UI.
 * This application uses REST API integration instead of direct database access,
 * following hexagonal architecture principles.
 */
@SpringBootApplication(
    exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class},
    scanBasePackages = {"org.litethinking.supermercado.web"}
)
public class WebApplication {

    /**
     * Main method to start the application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
