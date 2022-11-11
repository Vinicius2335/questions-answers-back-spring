package com.viniciusvieira.questionsanswers.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusvieira.questionsanswers.api.openapi.controller.ProfessorControllerOpenApi;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.services.ProfessorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*", maxAge = 3600)
public class ProfessorController implements ProfessorControllerOpenApi {
	private final ProfessorService professorService;
	
	@Override
    @GetMapping("/{idProfessor}")
    public ResponseEntity<ProfessorModel> getProfessorById(@PathVariable Long idProfessor){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(professorService.findByIdOrThrownProfessorNotFoundException(idProfessor));
    }
}
