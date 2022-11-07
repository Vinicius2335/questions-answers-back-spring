package com.viniciusvieira.questionsanswers.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusvieira.questionsanswers.domain.excepiton.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.services.QuestionAssignmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor/course/assignment/question-assignment")
@RequiredArgsConstructor
@Tag(name = "Question Assignment", description = "Operations to associate questions to an assigment")
public class QuestionAssignmentController {
	private final QuestionAssignmentService questionAssignmentService;
	
	@Operation(summary = "Find Question By Id",
			description = "Return valid questions for that course (valid questions are questions with at least two choices "
					+ "and one of the choices is correct and it is not already associated with that assigment)",
			responses = {
					@ApiResponse(responseCode = "200", description = "When A Question Found Successful"),
					@ApiResponse(responseCode = "404", description = "When No Question Was Found")
			})
	@GetMapping("/{courseId}/{assignmentId}")
	public ResponseEntity<List<QuestionModel>> findQuestionById(@PathVariable Long courseId,
			@PathVariable Long assignmentId){
		List<QuestionModel> questionsList = questionAssignmentService.listValidQuestionsByCourseNotAssociatedWithAnAssignment
				(courseId, assignmentId);
		
		List<QuestionModel> validQuestions = questionsList.stream().filter(question -> hasMoreThanOneChoices(question) 
				&& hasOnlyCorrectAnswer(question)).toList();
		
		if (validQuestions.isEmpty()) {
			throw new QuestionNotFoundException("Question Not Found!");
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(validQuestions);
	}

	private boolean hasOnlyCorrectAnswer(QuestionModel question) {
		return question.getChoices().stream().filter(ChoiceModel::isCorrectAnswer).count() == 1;
	}

	private boolean hasMoreThanOneChoices(QuestionModel question) {
		return question.getChoices().size() > 1;
	}
	
}
