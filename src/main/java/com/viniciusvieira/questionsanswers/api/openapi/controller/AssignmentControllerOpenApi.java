package com.viniciusvieira.questionsanswers.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.viniciusvieira.questionsanswers.api.representation.models.AssignmentDto;
import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Assignment")
public interface AssignmentControllerOpenApi {
	
	@Operation(summary = "Find assignment by his Id" , description = "Return a assignment based on it's id", 
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Assignment Not Found By ID",
					content = @Content(schema = @Schema(ref = "ExceptionDetails")))
			})
	ResponseEntity<AssignmentModel> getAssignmentById(@Parameter(description = "id of a assignment",
			example = "1", required = true) Long idAssignment);
	
	
	@Operation(summary = "Find assignment by title", description = "Return a list of assignments related to id coursen and professor",
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Course Not Found",
							content = @Content(schema = @Schema(ref = "ExceptionDetails"))),
					@ApiResponse(responseCode = "404", description = "When Assignment List is Empty",
							content = @Content(schema = @Schema(ref = "ExceptionDetails")))	
			})
	ResponseEntity<List<AssignmentModel>> findByCourseAndTitle(@Parameter(description = "id of a course",
			example = "1", required = true) Long idCourse ,
			@Parameter(description = "title of a assignment", example = "Java Exam" , required = false)
			String title);
	
	
	@Operation(summary = "Save Assignment", description = "Insert assignment in the database", responses = {
			@ApiResponse(responseCode = "201", description = "When Successful"),
			@ApiResponse(responseCode = "404", description = "When Course Not Found",
					content = @Content(schema = @Schema(ref = "ExceptionDetails"))),
			@ApiResponse(responseCode = "400", description = "When Have a Assignment field Empty or Null",
					content = @Content(schema = @Schema(ref = "ExceptionDetails")))
	})
	ResponseEntity<AssignmentModel> save(@RequestBody(description = "representation of a new course", required = true)
			AssignmentDto assignmentDto);
	
	
	@Operation(summary = "Delete Assignment", description = "Remove assignment in the database", responses = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "404", description = "When Assignment Not Found",
					content = @Content(schema = @Schema(ref = "ExceptionDetails")))
	})
	ResponseEntity<Object> delete(@Parameter(description = "id of a assignment", example = "1", required = true)
			Long idAssignment);
	
	
	@Operation(summary = "Update Assignment", description = "Update assignment in the database", responses = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "400", description = "When Assignment Title is Null or Empty",
					content = @Content(schema = @Schema(ref = "ExceptionDetails"))),
			@ApiResponse(responseCode = "404", description = "When Course Not Found",
					content = @Content(schema = @Schema(ref = "ExceptionDetails"))),
			@ApiResponse(responseCode = "404", description = "When Assignment Not Found",
					content = @Content(schema = @Schema(ref = "ExceptionDetails")))
	})
	ResponseEntity<Object> update(@Parameter(description = "id of a assignment", example = "1", required = true)
			Long idAssignment,
			@RequestBody(description = "Representation of a assignment with updated data", required = true)
			AssignmentDto assignmentDto);
}
