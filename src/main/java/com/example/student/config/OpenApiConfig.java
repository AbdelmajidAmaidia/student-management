package com.example.student.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
<<<<<<< HEAD
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Student Management API")
                .version("1.0.0")
                .description("Professional Spring Boot student management system"));
=======
    public OpenAPI studentManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Management API")
                        .description("Professional REST API for managing student records")
                        .version("1.0.0"));
>>>>>>> 3901877 (from vs code)
    }
}
