package com.chamo.chamotech.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI chamoTechOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("ChamoTech API - TechAndes")
                .description("REST API for catalog and orders: customers, categories, tags, products and orders.")
                .version("1.0.0"));
    }
}
