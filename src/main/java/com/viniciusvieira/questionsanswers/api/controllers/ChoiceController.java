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

import com.viniciusvieira.questionsanswers.api.openapi.controller.ChoiceControllerOpenApi;
import com.viniciusvieira.questionsanswers.api.representation.models.ChoiceDto;
import com.viniciusvieira.questionsanswers.domain.exception.ChoiceNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.services.ChoiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor/course/question/choice")
@RequiredArgsConstructor
public class ChoiceController implements ChoiceControllerOpenApi {
	private final ChoiceService choiceService;
	
	@Override
	@GetMapping("/list/{idQuestion}")
	public ResponseEntity<List<ChoiceModel>> getListChoices(@PathVariable Long idQuestion){
		List<ChoiceModel> listChoiceByQuestion = choiceService.listChoiceByQuestion(idQuestion);
		
		if(listChoiceByQuestion.isEmpty()) {
			throw new ChoiceNotFoundException("Choice Not Found by Id Question");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(listChoiceByQuestion);
	}
	
	
	@Override
	@GetMapping("/{idChoice}")
	public ResponseEntity<ChoiceModel> getChoiceById(@PathVariable Long idChoice){
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(choiceService.getChoiceByIdOrThrowChoiceNotFoundException(idChoice));
	}
	

	@Override
	@GetMapping("/list/{idQuestion}/")
	// /api/professor/course/question/choice/1/?title=" "
	public ResponseEntity<List<ChoiceModel>> findByTitle(@PathVariable Long idQuestion ,
			@RequestParam(required = false, defaultValue = "") String title){
		List<ChoiceModel> listChoice = choiceService
				.findByQuestionAndTitle(idQuestion, title);
		
		if (listChoice.isEmpty()) {
			throw new ChoiceNotFoundException("Choice List is Empty");
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(listChoice);
	}
	

	@Override
	@PostMapping
	public ResponseEntity<ChoiceModel> save(@RequestBody @Valid ChoiceDto choiceDto){
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(choiceService.save(choiceDto));
	}
	
	

	@Override
	@Transactional
	@PutMapping("/{idChoice}")
	public ResponseEntity<Object> replace(@PathVariable Long idChoice, @RequestBody @Valid ChoiceDto choiceDto){
		choiceService.replace(idChoice, choiceDto);
		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.body("Choice updated successfully");
	}
	

	@Override
	@DeleteMapping("/{idChoice}")
	public ResponseEntity<Object> delete(@PathVariable Long idChoice){
		choiceService.delete(idChoice);
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.body("Choice deleted successfully");
	}
}
