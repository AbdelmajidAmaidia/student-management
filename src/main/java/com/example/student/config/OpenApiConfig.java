package com.example.student.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI studentManagementApi() {
        return new OpenAPI().info(new Info()
                .title("Student Management API")
                .description("Enterprise-ready API for student management and CI/CD validation")
                .version("1.0.0")
                .contact(new Contact().name("Platform Team").email("platform@example.com"))
                .license(new License().name("MIT")));
    }
}
