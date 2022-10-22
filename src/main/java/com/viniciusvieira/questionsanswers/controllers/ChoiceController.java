package com.viniciusvieira.questionsanswers.controllers;

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

import com.viniciusvieira.questionsanswers.dtos.ChoiceDto;
import com.viniciusvieira.questionsanswers.excepiton.ChoiceNotFoundException;
import com.viniciusvieira.questionsanswers.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.services.ChoiceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor/course/question/choice")
@RequiredArgsConstructor
@Tag(name = "Choice", description = "Operations related to question's choice")
public class ChoiceController {
	private final ChoiceService choiceService;
	
	@Operation(summary = "Find Choice by Id Question", description = "Return a list of choices related to the questions",
			responses = {
					@ApiResponse(responseCode = "200", description = "When List of Choice Found Successful"),
					@ApiResponse(responseCode = "404", description = "When No Choice Was Found")
			})
	@GetMapping("/list/{idQuestion}")
	public ResponseEntity<List<ChoiceModel>> getListChoices(@PathVariable Long idQuestion){
		List<ChoiceModel> listChoiceByQuestion = choiceService.listChoiceByQuestion(idQuestion);
		
		if(listChoiceByQuestion.isEmpty()) {
			throw new ChoiceNotFoundException("Choice Not Found by Id Question");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(listChoiceByQuestion);
	}
	
	
	@Operation(summary = "Created Choice", description = "if the choice's correctAnswer is true, all other"
			+ " choice's correctAnswer related to this questions will be updated to false", responses = {
					@ApiResponse(responseCode = "201", description = "When Successful"),
					@ApiResponse(responseCode = "400", description = "When ChoiceDto have Invalid Fields"),
					@ApiResponse(responseCode = "404", description = "When Question Not Found")
			})
	@PostMapping
	public ResponseEntity<ChoiceModel> save(@RequestBody @Valid ChoiceDto choiceDto){
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(choiceService.save(choiceDto));
	}
	
	
	@Operation(summary = "Update Choice", description = "if the choice's correctAnswer is true, all other"
			+ " choice's correctAnswer related to this questions will be updated to false", responses = {
					@ApiResponse(responseCode = "201", description = "When Successful"),
					@ApiResponse(responseCode = "400", description = "When ChoiceDto have Invalid Fields"),
					@ApiResponse(responseCode = "404", description = "When Question Not Found")
	})
	@Transactional
	@PutMapping("/{idChoice}")
	public ResponseEntity<Object> replace(@PathVariable Long idChoice, @RequestBody @Valid ChoiceDto choiceDto){
		choiceService.replace(idChoice, choiceDto);
		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.body("Choice updated successfully");
	}
	
	@Operation(summary = "Delete Choice", description = "Remove choice in the database", responses = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "404", description = "When Choice or Question Not Found")
	})
	@DeleteMapping("/{idChoice}")
	public ResponseEntity<Object> delete(@PathVariable Long idChoice){
		choiceService.delete(idChoice);
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.body("Choice deleted successfully");
	}
}
