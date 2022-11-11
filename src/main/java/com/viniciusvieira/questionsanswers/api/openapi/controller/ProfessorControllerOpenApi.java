package com.viniciusvieira.questionsanswers.api.openapi.controller;

import org.springframework.http.ResponseEntity;

import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Professor")
public interface ProfessorControllerOpenApi {
	
    @Operation(summary = "Find professor by his Id", description = "Return a professor based on it's id",
    		responses = {
    		@ApiResponse(responseCode = "200", description = "When Successful"),
    		@ApiResponse(responseCode = "404", description = "When Professor Not Found",
    		content = @Content(schema = @Schema(ref = "Response")))
    })
	ResponseEntity<ProfessorModel> getProfessorById(@Parameter(description = "id of a professor", example = "1",
			required = true) Long idProfessor);
}
