package com.viniciusvieira.questionsanswers.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.viniciusvieira.questionsanswers.api.representation.models.ChoiceDto;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Choice")
public interface ChoiceControllerOpenApi {
	
	@Operation(summary = "Find Choice by Id Question", description = "Return a list of choices related to the questions",
			responses = {
					@ApiResponse(responseCode = "200", description = "When List of Choice Found Successful"),
					@ApiResponse(responseCode = "404", description = "When No Choice Was Found",
					content = @Content(schema = @Schema(ref = "ExceptionDetails")))
			})
	ResponseEntity<List<ChoiceModel>> getListChoices(@Parameter(description = "id of a question", example = "1",
			required = true) Long idQuestion);
	
	
	@Operation(summary = "Find choice by his Id" , description = "Return a achoice based on it's id", 
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Choice Not Found By ID",
							content = @Content(schema = @Schema(ref = "ExceptionDetails")))
			})
	ResponseEntity<ChoiceModel> getChoiceById(@Parameter(description = "id of a choice", example = "1",
			required = true) Long id);
	
	
	@Operation(summary = "Find choices by question and title", description = "Return a list of choices related to professor",
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Choice List is Empty or Question"
							+ " Not Found", content = @Content(schema = @Schema(ref = "ExceptionDetails")))	
			})
	ResponseEntity<List<ChoiceModel>> findByTitle(@Parameter(description = "id of a question", example = "1",
			required = true) Long idQuestion ,
			@Parameter(description = "title of a question",
			example = "which of these languages ​​is object oriented?", required = false) String title);
	
	
	@Operation(summary = "Created Choice", description = "if the choice's correctAnswer is true, all other"
			+ " choice's correctAnswer related to this questions will be updated to false", responses = {
					@ApiResponse(responseCode = "201", description = "When Successful"),
					@ApiResponse(responseCode = "400", description = "When ChoiceDto have Invalid Fields",
							content = @Content(schema = @Schema(ref = "ExceptionDetails"))),
					@ApiResponse(responseCode = "404", description = "When Question Not Found",
							content = @Content(schema = @Schema(ref = "ExceptionDetails")))
			})
	ResponseEntity<ChoiceModel> save(@RequestBody(description = "representation of a new choice", required = true)
			ChoiceDto choiceDto);
	
	
	@Operation(summary = "Update Choice", description = "if the choice's correctAnswer is true, all other"
			+ " choice's correctAnswer related to this questions will be updated to false", responses = {
					@ApiResponse(responseCode = "204", description = "When Successful"),
					@ApiResponse(responseCode = "400", description = "When ChoiceDto have Invalid Fields",
							content = @Content(schema = @Schema(ref = "ExceptionDetails"))),
					@ApiResponse(responseCode = "404", description = "When Choice or Question Not Found",
							content = @Content(schema = @Schema(ref = "ExceptionDetails")))
	})
	ResponseEntity<Object> replace(@Parameter(description = "id of a choice", example = "1", required = true)
			Long idChoice, @RequestBody(description = "Representation of a choice with updated data")
			ChoiceDto choiceDto);
	
	
	@Operation(summary = "Delete Choice", description = "Remove choice in the database", responses = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "404", description = "When Choice or Question Not Found",
					content = @Content(schema = @Schema(ref = "ExceptionDetails"))),
			@ApiResponse(responseCode = "409", description = "When Choice is Associated with a QuestionAssignment",
					content = @Content(schema = @Schema(ref = "ExceptionDetails")))
	})
	ResponseEntity<Object> delete(@Parameter(description = "id of a choice", example = "1", required = true)
			Long idChoice);
}
