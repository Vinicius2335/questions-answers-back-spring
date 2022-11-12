package com.viniciusvieira.questionsanswers.core.springdoc;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.viniciusvieira.questionsanswers.api.exceptionhandler.ExceptionDetails;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;


/**
 * 
 * @author Vinicius Vieira
 * Obs: Erro nas responses do Swagger, não consegui resolver.
 *
 */

@Configuration
@SecurityScheme(name = "bearerAuth",
			type = SecuritySchemeType.HTTP,
			scheme = "bearer",
			description = "Provide the JWT token. JWT token can be obtained from the Login API. "
					+ "For testing, use the credentials <strong>vinicius/devdojo</strong>",
			bearerFormat = "JWT")
public class OpenAPI3Config {
	
	private static final String badRequestResponse = "BadRequestResponse";
	private static final String notFoundResponse = "NotFoundResponse";
	private static final String notModifiedResponse = "NotModifiedResponse";
	private static final String internalServerErrorResponse = "InternalServerErrorResponse";
	
	@Bean
    public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info()
                        .title("Exam generator by Vinicius")
                        .version("1.0")
                        .description("Software to generate exams based on questions")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                ).externalDocs(new ExternalDocumentation()
                        .description("Teste Description External Documentation OpenAPI")
                        .url("https://google.com.br")
                ).servers(Arrays.asList(
                		new Server().url("http://localhost:8080").description("Development")
                )).tags(Arrays.asList(
                        new Tag().name("Course").description("Operations related to professors course"),
                        new Tag().name("Assignment").description("Operations related to course's assignment"),
                        new Tag().name("Choice").description("Operations related to question's choice"),
                        new Tag().name("Professor").description("Operations related to professors"),
                        new Tag().name("Question Assignment").description("Operations to associate questions to an assigment"),
                        new Tag().name("Question").description("Operations related to courses question")
                )).components(new Components()
                        .schemas(gerarSchemas())
                        .responses(gerarResponses())
                ).security(Arrays.asList(new SecurityRequirement().addList("bearerAuth")));
	}
	
	
	@Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> {
            openApi.getPaths()
                    .values()
                    .forEach(pathItem -> pathItem.readOperationsMap()
                            .forEach((httpMethod, operation) -> {
                                ApiResponses responses = operation.getResponses();
                                switch (httpMethod) {
                                    case GET:
                                    	responses.addApiResponse("404", new ApiResponse()
                                        		.$ref(notFoundResponse));
                                        responses.addApiResponse("500", new ApiResponse()
                                        		.$ref(internalServerErrorResponse));
                                        break;
                                    case POST:
                                    	responses.addApiResponse("304", new ApiResponse()
                                         		.$ref(notModifiedResponse));
                                        responses.addApiResponse("400", new ApiResponse()
                                        		.$ref(badRequestResponse));
                                        responses.addApiResponse("404", new ApiResponse()
                                        		.$ref(notFoundResponse));
                                        responses.addApiResponse("500", new ApiResponse()
                                        		.$ref(internalServerErrorResponse));
                                        break;
                                    case PUT:
                                        responses.addApiResponse("400", new ApiResponse()
                                        		.$ref(badRequestResponse));
                                        responses.addApiResponse("404", new ApiResponse()
                                        		.$ref(notFoundResponse));
                                        responses.addApiResponse("500", new ApiResponse()
                                        		.$ref(internalServerErrorResponse));
                                        break;
                                    case DELETE:
                                    	responses.addApiResponse("404", new ApiResponse()
                                         		.$ref(notFoundResponse));
                                        responses.addApiResponse("500", new ApiResponse()
                                        		.$ref(internalServerErrorResponse));
                                        break;
                                    default:
                                        responses.addApiResponse("500", new ApiResponse()
                                        		.$ref(internalServerErrorResponse));
                                        break;
                                }
                            })
                    );
        };
    }
    
    @SuppressWarnings("rawtypes")
	private Map<String, Schema> gerarSchemas() {
        final Map<String, Schema> schemaMap = new HashMap<>();

        Map<String, Schema> problemSchema = ModelConverters.getInstance().read(ExceptionDetails.class);
        Map<String, Schema> problemFieldSchema = ModelConverters.getInstance().read(ExceptionDetails.Field.class);
        

        schemaMap.putAll(problemSchema);
        schemaMap.putAll(problemFieldSchema);

        return schemaMap;
    }

    private Map<String, ApiResponse> gerarResponses() {
        final Map<String, ApiResponse> apiResponseMap = new HashMap<>();

        Content content = new Content()
                .addMediaType(APPLICATION_JSON_VALUE,
                        new MediaType().schema(new Schema<ExceptionDetails>().$ref("ExceptionDetails")));

        apiResponseMap.put(badRequestResponse, new ApiResponse()
                .description("Invalid Request")
                .content(content));
        
        apiResponseMap.put(notFoundResponse, new ApiResponse()
                .description("Resource not found")
                .content(content));
        
        apiResponseMap.put(notModifiedResponse, new ApiResponse()
        		.description("Resource cannot be modified")
        		.content(content));

        apiResponseMap.put(internalServerErrorResponse, new ApiResponse()
                .description("Internal server error")
                .content(content));

        return apiResponseMap;
    }
}
