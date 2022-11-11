package com.viniciusvieira.questionsanswers.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.viniciusvieira.questionsanswers.api.representation.models.QuestionDto;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Question")
public interface QuestionControllerOpenApi {
	
	@Operation(summary = "Find question by his Id" , description = "Return a question based on it's id", 
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Question Not Found By ID",
					content = @Content(schema = @Schema(ref = "Response")))
			})
	ResponseEntity<QuestionModel> getQuestionById(@Parameter(description = "id of a question", example = "1",
			required = true) Long idQuestion);
	
	
	@Operation(summary = "Find questions by title", description = "Return a list of questions related to professor",
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Question List is Empty",
							content = @Content(schema = @Schema(ref = "Response")))	
			})
	ResponseEntity<List<QuestionModel>> findByTitle(@Parameter(description = "id of a course", example = "1",
			required = true) Long idCourse,
			@Parameter(description = "title of a question", example = "What's your name?",
			required = true) String title);
	
	
	@Operation(summary = "Save Question", description = "Insert question in the database", responses = {
			@ApiResponse(responseCode = "201", description = "When Successful"),
			@ApiResponse(responseCode = "400", description = "When Have a Question field Empty",
					content = @Content(schema = @Schema(ref = "Response")))
	})
	ResponseEntity<QuestionModel> save(@RequestBody(description = "representation of a new question",
			required = true) QuestionDto questionDto);
	
	
	@Operation(summary = "Delete Question", description = "Remove question in the database and all related choices", responses = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "404", description = "When Question Not Found",
					content = @Content(schema = @Schema(ref = "Response")))
	})
	ResponseEntity<Object> delete(@Parameter(description = "id of a question", example = "1",
			required = true) Long idQuestion);
	
	
	@Operation(summary = "Update Question", description = "Update question in the database", responses = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "400", description = "When Question Title is Null or Empty",
					content = @Content(schema = @Schema(ref = "Response"))),
			@ApiResponse(responseCode = "404", description = "When Question Not Found",
					content = @Content(schema = @Schema(ref = "Response")))
	})
	ResponseEntity<Object> update(@Parameter(description = "id of a question", example = "1",
			required = true) Long idQuestion,
			@RequestBody(description = "Representation of a question with updated data", required = true)
			QuestionDto questionDto);
}
