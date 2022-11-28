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

import com.viniciusvieira.questionsanswers.api.openapi.controller.QuestionControllerOpenApi;
import com.viniciusvieira.questionsanswers.api.representation.models.QuestionDto;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.services.CascadeDeleteService;
import com.viniciusvieira.questionsanswers.domain.services.QuestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor/course/question")
@RequiredArgsConstructor
public class QuestionController implements QuestionControllerOpenApi {
	private final QuestionService questionService;
	private final CascadeDeleteService cascadeDeleteService; 
	

	@Override
	@GetMapping("/{idQuestion}")
	public ResponseEntity<QuestionModel> getQuestionById(@PathVariable Long idQuestion){
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(questionService.findByIdOrThrowQuestionNotFoundException(idQuestion));
	}
	
	
	@Override
	@GetMapping("/list/{idCourse}/")
	// /api/professor/course/question/1/?title=" "
	public ResponseEntity<List<QuestionModel>> findByTitle(@PathVariable Long idCourse ,
			@RequestParam(required = false, defaultValue = "") String title){
		List<QuestionModel> listQuestion = questionService
				.findByCourseAndTitle(idCourse, title);
		
		if (listQuestion.isEmpty()) {
			throw new QuestionNotFoundException("Question List is Empty");
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(listQuestion);
	}
	
	
	@Override
	@PostMapping
	public ResponseEntity<QuestionModel> save(@RequestBody @Valid QuestionDto questionDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(questionService.save(questionDto));
	}
	
	@Override
	@Transactional
	@DeleteMapping("/{idQuestion}")
	public ResponseEntity<Object> delete(@PathVariable Long idQuestion){
		cascadeDeleteService.deleteQuestionAndAllRelatedEntities(idQuestion);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Question deleted successfully");
	}
	
	
	@Override
	@PutMapping("/{idQuestion}")
	public ResponseEntity<Object> update(@PathVariable Long idQuestion, @RequestBody @Valid QuestionDto questionDto){
		questionService.replace(idQuestion, questionDto);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Question updated successfully");
	}
}
