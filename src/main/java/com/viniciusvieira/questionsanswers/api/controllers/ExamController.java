package com.viniciusvieira.questionsanswers.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusvieira.questionsanswers.api.openapi.controller.ExamControllerOpenApi;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.services.ExamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/student/exam")
@RequiredArgsConstructor
public class ExamController implements ExamControllerOpenApi {
	private final ExamService examService;
	
	@Override
	@GetMapping("/questions/{accessCode}")
	public ResponseEntity<List<QuestionModel>> findAllQuestionsByAccessCode(@PathVariable String accessCode) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(examService.findAllQuestionsByAccessCode(accessCode));
	}

	@Override
	@GetMapping("/choices/{accessCode}")
	public ResponseEntity<List<ChoiceModel>> findAllChoicesByAccessCode(@PathVariable String accessCode) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(examService.findAllChoicesByAccessCode(accessCode));
	}
	
}