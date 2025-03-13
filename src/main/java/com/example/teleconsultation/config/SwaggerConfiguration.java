package com.example.teleconsultation.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@OpenAPIDefinition
@Slf4j
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("spring_oauth2", new io.swagger.v3.oas.models.security.SecurityScheme()
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.OAUTH2)
                                .description("Oauth2 flow")
                                .flows(new io.swagger.v3.oas.models.security.OAuthFlows()
                                        .clientCredentials(new io.swagger.v3.oas.models.security.OAuthFlow()
                                        )))
                )
                .security(Arrays.asList(
                        new SecurityRequirement().addList("spring_oauth2")))
                .info(new Info()
                        .title("Book Application API")
                        .description("This is a consultation Spring Boot RESTful service")
                        .termsOfService("terms")
                        .contact(new Contact().email("ikrame.labzioui@gmail.com").name("Developer: Labzioui Ikrame"))
                        .license(new License().name("GNU"))
                        .version("1.0")
                );
    }


}
