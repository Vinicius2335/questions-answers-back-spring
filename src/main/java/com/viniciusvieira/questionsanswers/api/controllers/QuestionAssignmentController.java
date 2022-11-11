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

import com.viniciusvieira.questionsanswers.api.openapi.controller.QuestionAssignmentOpenApi;
import com.viniciusvieira.questionsanswers.api.representation.models.QuestionAssignmentDto;
import com.viniciusvieira.questionsanswers.domain.models.QuestionAssignmentModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.services.CascadeDeleteService;
import com.viniciusvieira.questionsanswers.domain.services.QuestionAssignmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor/course/assignment/questionassignment")
@RequiredArgsConstructor
public class QuestionAssignmentController implements QuestionAssignmentOpenApi{
	private final QuestionAssignmentService questionAssignmentService;
	private final CascadeDeleteService cascadeDeleteService;
	
	@Override
	@GetMapping("/list/{courseId}/{assignmentId}")
	public ResponseEntity<List<QuestionModel>> findQuestionById(@PathVariable Long courseId,
			@PathVariable Long assignmentId){
		List<QuestionModel> validQuestions = questionAssignmentService.findQuestionById(courseId, assignmentId);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(validQuestions);
	}
	

	@Override
	@GetMapping("/list/{idAssignment}")
	public ResponseEntity<List<QuestionAssignmentModel>> listByAssignment(@PathVariable Long idAssignment){
		List<QuestionAssignmentModel> questionAssignments = questionAssignmentService
				.listByAssignment(idAssignment);
		
		return ResponseEntity.status(HttpStatus.OK).body(questionAssignments);
	}
	

	@Override
	@PostMapping
	public ResponseEntity<QuestionAssignmentModel> save(@RequestBody @Valid QuestionAssignmentDto questionAssignmentDto){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(questionAssignmentService.save(questionAssignmentDto));
	}
	
	
	@Override
	@PutMapping("/{idQuestionAssignment}")
	public ResponseEntity<Object> update(@PathVariable Long idQuestionAssignment,
			@RequestBody @Valid QuestionAssignmentDto questionAssignmentDto){
		questionAssignmentService.replace(idQuestionAssignment, questionAssignmentDto);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Question updated successfully");
	}
	
	
	@Override
	@Transactional
	@DeleteMapping("/{idQuestionAssignment}")
	public ResponseEntity<Object> delete(@PathVariable Long idQuestionAssignment){
		cascadeDeleteService.deleteQuestionAssignmentAndAllRelatedEntities(idQuestionAssignment);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("QuestionAssignment deleted successfully");
	}
	
}
