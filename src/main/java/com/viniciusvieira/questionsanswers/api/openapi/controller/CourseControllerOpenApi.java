package com.viniciusvieira.questionsanswers.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.viniciusvieira.questionsanswers.api.representation.models.CourseDto;
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Course")
public interface CourseControllerOpenApi {
	
	@Operation(summary = "Find course by his Id", description = "Return a course based on it's id",
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Course Not Found By ID", 
					content = @Content(schema = @Schema(ref = "Response")))	
			})
	ResponseEntity<CourseModel> getCourseById(@Parameter(description = "id of a course", example = "1",
			required = true) Long id);
	
	
	@Operation(summary = "Find courses by name", description = "Return a list of courses related to professor",
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Course List is Empty",
					content = @Content(schema = @Schema(ref = "Response")))	
			})
	ResponseEntity<List<CourseModel>> findByName(@Parameter(description = "name of a course", example = "Java",
			required = false) String name);
	
	
	@Operation(summary = "Save Course", description = "Insert course in the database",
			responses = {
					@ApiResponse(responseCode = "201", description = "When Successful"),
					@ApiResponse(responseCode = "400", description = "When Have a Course field Empty",
					content = @Content(schema = @Schema(ref = "Response")))
	})
	ResponseEntity<CourseModel> save(@RequestBody(description = "representation of a new course", required = true)
			CourseDto courseDto);
	
	
	@Operation(summary = "Delete Course", description = "Remove course in the database and all related questions"
			+ " and choices",
			responses = {
					@ApiResponse(responseCode = "204", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Course Not Found",
					content = @Content(schema = @Schema(ref = "Response")))
	})
	ResponseEntity<Object> delete(@Parameter(description = "id of a course", example = "1",
			required = true) Long id);
	
	
	@Operation(summary = "Update Couse", description = "Update course in the database",
			responses = {
					@ApiResponse(responseCode = "204", description = "When Successful"),
					@ApiResponse(responseCode = "400", description = "When Course Name is Null or Empty",
					content = @Content(schema = @Schema(ref = "Response"))),
					@ApiResponse(responseCode = "404", description = "When Course Not Found",
					content = @Content(schema = @Schema(ref = "Response")))
	})
	ResponseEntity<Object> update(@Parameter(description = "id of a course", example = "1",
			required = true) Long id,
			@RequestBody(description = "Representation of a course with updated data", required = true)
			CourseDto courseDto);
}
