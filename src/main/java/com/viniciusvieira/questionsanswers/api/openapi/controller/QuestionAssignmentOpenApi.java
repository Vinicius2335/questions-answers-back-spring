package com.viniciusvieira.questionsanswers.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.viniciusvieira.questionsanswers.api.representation.models.QuestionAssignmentDto;
import com.viniciusvieira.questionsanswers.domain.models.QuestionAssignmentModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Question Assignment")
public interface QuestionAssignmentOpenApi {
	
	@Operation(summary = "Find Question By Id",
			description = "Return valid questions for that course (valid questions are questions with at least two choices "
					+ "and one of the choices is correct and it is not already associated with that assigment)",
			responses = {
					@ApiResponse(responseCode = "200", description = "When A Question Found Successful"),
					@ApiResponse(responseCode = "404", description = "When No Question Was Found",
							content = @Content(schema = @Schema(ref = "ExceptionDetails")))
			})
	ResponseEntity<List<QuestionModel>> findQuestionById(@Parameter(description = "id of a course",
			example = "1", required = true) Long courseId,
			@Parameter(description = "id of a assignment", example = "1", required = true) Long assignmentId);
	
	@Operation(summary = "ListAll QuestionAssignment", description = "List all questionAssignment associated with assignmentId",
			responses = {
					@ApiResponse(responseCode = "204", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Assignment Not Found",
							content = @Content(schema = @Schema(ref = "ExceptionDetails")))
			})
	ResponseEntity<List<QuestionAssignmentModel>> listByAssignment(@Parameter(description = "id of a assignment",
			example = "1", required = true) Long idAssignment);
	
	
	@Operation(summary = "Save QuestionAssignment", description = "Associate a question to an assignment and return the QuestionAssignment created",
			responses = {
					@ApiResponse(responseCode = "201", description = "When Successful"),
					@ApiResponse(responseCode = "304", description = "When Question Assignment Association Already Exists",
							content = @Content(schema = @Schema(ref = "ExceptionDetails"))),
					@ApiResponse(responseCode = "400", description = "When Have a QuestionAssignmentDto field Empty",
							content = @Content(schema = @Schema(ref = "ExceptionDetails"))),
					@ApiResponse(responseCode = "404", description = "When Question Not Found",
							content = @Content(schema = @Schema(ref = "ExceptionDetails"))),
					@ApiResponse(responseCode = "404", description = "When Assignment Not Found",
							content = @Content(schema = @Schema(ref = "ExceptionDetails")))
			})
	ResponseEntity<QuestionAssignmentModel> save(@RequestBody(description = "representation of a new questionAssignment",
			required = true) QuestionAssignmentDto questionAssignmentDto);
	
	
	@Operation(summary = "Update QuestionAssignment", description = "Update questionAssignment and return 204 no content with no body",
			responses = {
					@ApiResponse(responseCode = "204", description = "When Successful"),
					@ApiResponse(responseCode = "400", description = "When QuestionAssignmentDto Have Invalid Field",
							content = @Content(schema = @Schema(ref = "ExceptionDetails"))),
					@ApiResponse(responseCode = "404", description = "When QuestionAssignment Not Found",
							content = @Content(schema = @Schema(ref = "ExceptionDetails")))
			})
	ResponseEntity<Object> update(@Parameter(description = "id of a questionAssignment", example = "1",
			required = true) Long idQuestionAssignment,
			@RequestBody(description = "Representation of a questionAssignment with updated data", required = true)
			QuestionAssignmentDto questionAssignmentDto);
	
	
	@Operation(summary = "Delete QuestionAssignment", description = "Delete a specific question assignment to an assignment and return 204 no content body",
			responses = {
					@ApiResponse(responseCode = "204", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When QuestionAssignment Not Found",
							content = @Content(schema = @Schema(ref = "ExceptionDetails")))
	})
	ResponseEntity<Object> delete(@Parameter(description = "id of a questionAssignment", example = "1",
			required = true) Long idQuestionAssignment);
}
