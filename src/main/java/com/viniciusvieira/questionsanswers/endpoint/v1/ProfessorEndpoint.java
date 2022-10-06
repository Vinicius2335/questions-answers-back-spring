package com.viniciusvieira.questionsanswers.endpoint.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusvieira.questionsanswers.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.repositories.ProfessorRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("v1/professor")
@RequiredArgsConstructor
public class ProfessorEndpoint {
	private final ProfessorRepository professorRepository; 
	
    @GetMapping("/{id}")
    @Operation(summary = "Find professor by his Id", description = "Return a professor based on it's id")
    public ResponseEntity<ProfessorModel> getProfessorById(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(professorRepository.findById(id)
                		.orElseThrow(() -> new IllegalArgumentException("Professor Id Invalid")));
    }
}
