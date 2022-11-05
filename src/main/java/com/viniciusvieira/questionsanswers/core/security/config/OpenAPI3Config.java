package com.viniciusvieira.questionsanswers.core.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
      info = @Info(title = "Exam generator by Vinicius", version = "1.0",
              license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
              termsOfService = "${tos.uri}",
              description = "Software to generate exams based on questions"),
      servers = {
              @Server(url = "http://localhost:8080", description = "Development"),
              @Server(url = "${api.server.url}", description = "Production")})
public class OpenAPI3Config {
	@Bean
	public OpenAPI customizeOpenAPI() {
	final String securitySchemeName = "bearerAuth";
	
    return new OpenAPI()
            .addSecurityItem(new SecurityRequirement()
                    .addList(securitySchemeName))
            .components(new Components()
                    .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .description(
                                    "Provide the JWT token. JWT token can be obtained from the Login API."
                                    + " For testing, use the credentials <strong>vinicius/devdojo</strong>")
                            .bearerFormat("JWT")));
	}
}
