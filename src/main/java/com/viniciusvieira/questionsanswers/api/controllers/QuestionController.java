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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusvieira.questionsanswers.api.representation.models.QuestionDto;
import com.viniciusvieira.questionsanswers.domain.excepiton.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.services.CascadeDeleteService;
import com.viniciusvieira.questionsanswers.domain.services.QuestionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor/course/question")
@RequiredArgsConstructor
@Tag(name = "Question", description = "Operations related to courses question")
public class QuestionController {
	private final QuestionService questionService;
	private final CascadeDeleteService cascadeDeleteService; 
	
	@Operation(summary = "Find question by his Id" , description = "Return a question based on it's id", 
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Question Not Found By ID")
			})
	@GetMapping("/{id}")
	public ResponseEntity<QuestionModel> getQuestionById(@PathVariable Long id){
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(questionService.findByIdOrThrowQuestionNotFoundException(id));
	}
	
	
	@Operation(summary = "Find questions by title", description = "Return a list of questions related to professor",
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Question List is Empty")	
			})
	@GetMapping("/list/{idCourse}/")
	// /api/professor/course/question/1/?title=" "
	public ResponseEntity<List<QuestionModel>> findByTitle(@PathVariable Long idCourse ,
			@RequestParam String title){
		ProfessorModel professor = questionService.extractProfessorFromToken();
		List<QuestionModel> listQuestion = questionService
				.findByCourseAndTitle(idCourse, title, professor.getIdProfessor());
		
		if (listQuestion.isEmpty()) {
			throw new QuestionNotFoundException("Question List is Empty");
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(listQuestion);
	}
	
	
	@Operation(summary = "Save Question", description = "Insert question in the database", responses = {
			@ApiResponse(responseCode = "201", description = "When Successful"),
			@ApiResponse(responseCode = "400", description = "When Have a Question field Empty")
	})
	@PostMapping
	public ResponseEntity<QuestionModel> save(@RequestBody @Valid QuestionDto questionDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(questionService.save(questionDto));
	}
	
	@Operation(summary = "Delete Question", description = "Remove question in the database and all related choices", responses = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "404", description = "When Question Not Found")
	})
	@Transactional
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id){
		cascadeDeleteService.cascadeDeleteQuestionAndChoice(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Question deleted successfully");
	}
	
	
	@Operation(summary = "Update Question", description = "Update question in the database", responses = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "400", description = "When Question Title is Null or Empty"),
			@ApiResponse(responseCode = "404", description = "When Question Not Found")
	})
	@PutMapping("/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid QuestionDto questionDto){
		questionService.replace(id, questionDto);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Question updated successfully");
	}
}
