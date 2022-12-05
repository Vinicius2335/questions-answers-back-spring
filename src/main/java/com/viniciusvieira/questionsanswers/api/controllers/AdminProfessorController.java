package com.viniciusvieira.questionsanswers.api.controllers;

import com.viniciusvieira.questionsanswers.api.openapi.controller.AdminProfessorControllerOpenApi;
import com.viniciusvieira.questionsanswers.api.representation.models.ApplicationUserDto;
import com.viniciusvieira.questionsanswers.api.representation.models.ProfessorDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.ApplicationUserRequestBody;
import com.viniciusvieira.questionsanswers.api.representation.requests.ProfessorRequestBody;
import com.viniciusvieira.questionsanswers.domain.services.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/professor")
@RequiredArgsConstructor
public class AdminProfessorController implements AdminProfessorControllerOpenApi {
    private final ProfessorService professorService;

    @Override
    @PostMapping
    public ResponseEntity<ProfessorDto> save(@Valid @RequestBody ProfessorRequestBody professorRequestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.saveProfessor(professorRequestBody));
    }

    @Override
    @PostMapping("/{idProfessor}")
    public ResponseEntity<ApplicationUserDto> saveProfessorApplicationUser(@PathVariable Long idProfessor
            ,@Valid @RequestBody ApplicationUserRequestBody applicationUserRequestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.saveApplicationUserProfessor(idProfessor,
                applicationUserRequestBody));
    }

    @Override
    @GetMapping("/professors")
    public ResponseEntity<List<ProfessorDto>> findByName(@RequestParam(required = false, defaultValue = "") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(professorService.findByName(name));
    }

    @Override
    @PutMapping("/{idProfessor}")
    public ResponseEntity<ProfessorDto> replace(@PathVariable Long idProfessor, @Valid @RequestBody ProfessorRequestBody
            professorRequestBody) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(professorService.replace(idProfessor, professorRequestBody));
    }

    @Override
    @DeleteMapping("/{idProfessor}")
    public ResponseEntity<Object> delete(@PathVariable Long idProfessor) {
        professorService.delete(idProfessor);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Professor deleted successfully");
    }
}
//TEST