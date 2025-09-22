package org.litethinking.reto1cleanarchapicrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
    "org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.repository"
})
public class Reto1CleanArchApiCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(Reto1CleanArchApiCrudApplication.class, args);
    }

}
