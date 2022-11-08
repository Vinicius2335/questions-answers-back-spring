package com.viniciusvieira.questionsanswers.api.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusvieira.questionsanswers.api.representation.models.QuestionAssignmentDto;
import com.viniciusvieira.questionsanswers.domain.models.QuestionAssignmentModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.services.CascadeDeleteService;
import com.viniciusvieira.questionsanswers.domain.services.QuestionAssignmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor/course/assignment/questionassignment")
@RequiredArgsConstructor
@Tag(name = "Question Assignment", description = "Operations to associate questions to an assigment")
public class QuestionAssignmentController {
	private final QuestionAssignmentService questionAssignmentService;
	private final CascadeDeleteService cascadeDeleteService;
	
	@Operation(summary = "Find Question By Id",
			description = "Return valid questions for that course (valid questions are questions with at least two choices "
					+ "and one of the choices is correct and it is not already associated with that assigment)",
			responses = {
					@ApiResponse(responseCode = "200", description = "When A Question Found Successful"),
					@ApiResponse(responseCode = "404", description = "When No Question Was Found")
			})
	@GetMapping("/list/{courseId}/{assignmentId}")
	public ResponseEntity<List<QuestionModel>> findQuestionById(@PathVariable Long courseId,
			@PathVariable Long assignmentId){
		List<QuestionModel> validQuestions = questionAssignmentService.findQuestionById(courseId, assignmentId);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(validQuestions);
	}
	
	
	@Operation(summary = "ListAll QuestionAssignment", description = "List all questionAssignment associated with assignmentId",
			responses = {
					@ApiResponse(responseCode = "204", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Assignment Not Found")
			})
	@GetMapping("/list/{idAssignment}")
	public ResponseEntity<List<QuestionAssignmentModel>> listByAssignment(@PathVariable Long idAssignment){
		List<QuestionAssignmentModel> questionAssignments = questionAssignmentService
				.listByAssignment(idAssignment);
		
		return ResponseEntity.status(HttpStatus.OK).body(questionAssignments);
	}
	
	@Operation(summary = "Save QuestionAssignment", description = "Associate a question to an assignment and return the QuestionAssignment created",
			responses = {
					@ApiResponse(responseCode = "201", description = "When Successful"),
					@ApiResponse(responseCode = "304", description = "When Question Assignment Association Already Exists"),
					@ApiResponse(responseCode = "400", description = "When Have a QuestionAssignmentDto field Empty"),
					@ApiResponse(responseCode = "404", description = "When Question Not Found"),
					@ApiResponse(responseCode = "404", description = "When Assignment Not Found")
			})
	@PostMapping
	public ResponseEntity<QuestionAssignmentModel> save(@RequestBody @Valid QuestionAssignmentDto questionAssignmentDto){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(questionAssignmentService.save(questionAssignmentDto));
	}
	
	
	@Operation(summary = "Update QuestionAssignment", description = "Update questionAssignment and return 204 no content with no body",
			responses = {
					@ApiResponse(responseCode = "204", description = "When Successful"),
					@ApiResponse(responseCode = "400", description = "When QuestionAssignmentDto Have Invalid Field"),
					@ApiResponse(responseCode = "404", description = "When QuestionAssignment Not Found")
			})
	@PutMapping("/{idQuestionAssignment}")
	public ResponseEntity<Object> update(@PathVariable Long idQuestionAssignment,
			@RequestBody @Valid QuestionAssignmentDto questionAssignmentDto){
		questionAssignmentService.replace(idQuestionAssignment, questionAssignmentDto);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Question updated successfully");
	}
	
	
	@Operation(summary = "Delete QuestionAssignment", description = "Delete a specific question assignment to an assignment and return 204 no content body",
			responses = {
					@ApiResponse(responseCode = "204", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When QuestionAssignment Not Found")
	})
	@Transactional
	@DeleteMapping("/{idQuestionAssignment}")
	public ResponseEntity<Object> delete(@PathVariable Long idQuestionAssignment){
		cascadeDeleteService.deleteQuestionAssignmentAndAllRelatedEntities(idQuestionAssignment);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("QuestionAssignment deleted successfully");
	}
	
}
