package com.bibliotheque.gestion_bibliotheque.config;

<<<<<<< HEAD

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

=======
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Swagger/OpenAPI pour documenter l'API REST
 * de l'application de gestion de bibliothèque.
 *
 * Documentation accessible à : http://localhost:8080/swagger-ui.html
 */
>>>>>>> koussaila
@Configuration
public class SwaggerConfig {

    @Bean
<<<<<<< HEAD
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestion de Bibliothèque")
                        .version("1.0.0")
                        .description("API REST pour gérer une bibliothèque selon les principes de Clean Architecture")
                        .contact(new Contact()
                                .name("Équipe ESIEA")
                                .email("ramanadane@esiea.et.fr")));
=======
    public OpenAPI bibliothequeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestion de Bibliothèque")
                        .description("API REST pour la gestion d'une bibliothèque - Projet Architecture d'Application")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Équipe Projet")
                                .email("contact@bibliotheque.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
>>>>>>> koussaila
    }
}
