package com.viniciusvieira.questionsanswers.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.services.ProfessorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Professor", description = "Operations related to professors")
public class ProfessorController {
	private final ProfessorService professorService;
	
    @Operation(summary = "Find professor by his Id", description = "Return a professor based on it's id",
    		responses = {
    		@ApiResponse(responseCode = "200", description = "When Successful"),
    		@ApiResponse(responseCode = "404", description = "When Professor Not Found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorModel> getProfessorById(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(professorService.findByIdOrThrownProfessorNotFoundException(id));
    }
}
