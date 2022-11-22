package com.viniciusvieira.questionsanswers.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Exam")
public interface ExamControllerOpenApi {
	
	@Operation(summary = "Find all Questions by accessCode" , description = "Return all questions from "
			+ "question-assignment by the assignment access code", 
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Question Not Found",
					content = @Content(schema = @Schema(ref = "ExceptionDetails")))
			})
	ResponseEntity<List<QuestionModel>> findAllQuestionsByAccessCode(@Parameter(description = "accessCode of a assignment",
			example = "1234", required = true) String accessCode);
	
	@Operation(summary = "Find all Choices by accessCode" , description = "Return all questions based on the questions "
			+ "by the assignment access code", 
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Choices Not Found",
					content = @Content(schema = @Schema(ref = "ExceptionDetails")))
	})
	ResponseEntity<List<ChoiceModel>> findAllChoicesByAccessCode(@Parameter(description = "accessCode of a assignment",
	example = "1234", required = true) String accessCode);
}
