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

import com.viniciusvieira.questionsanswers.api.openapi.controller.AssignmentControllerOpenApi;
import com.viniciusvieira.questionsanswers.api.representation.models.AssignmentDto;
import com.viniciusvieira.questionsanswers.domain.exception.AssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;
import com.viniciusvieira.questionsanswers.domain.services.AssignmentService;
import com.viniciusvieira.questionsanswers.domain.services.CascadeDeleteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor/course/assignment")
@RequiredArgsConstructor
public class AssignmentController implements AssignmentControllerOpenApi{
	private final AssignmentService assignmentService;
	private final CascadeDeleteService cascadeDeleteService;
	

	@Override
	@GetMapping("/{idAssignment}")
	public ResponseEntity<AssignmentModel> getAssignmentById(@PathVariable Long idAssignment){
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(assignmentService.findAssignmentOrThrowsAssignmentNotFoundException(idAssignment));
	}
	
	
	@Override
	@GetMapping("/list/{idCourse}/")
	// /api/professor/course/assignement/1/?title=" "
	public ResponseEntity<List<AssignmentModel>> findByCourseAndTitle(@PathVariable Long idCourse ,
			@RequestParam(required = false, defaultValue = "") String title){
		List<AssignmentModel> listAssignment = assignmentService
				.findByCourseAndTitle(idCourse, title);
		
		if (listAssignment.isEmpty()) {
			throw new AssignmentNotFoundException("Assignment List is Empty");
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(listAssignment);
	}
	
	
	@Override
	@PostMapping
	public ResponseEntity<AssignmentModel> save(@RequestBody @Valid AssignmentDto assignmentDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(assignmentService.save(assignmentDto));
	}
	
	
	@Override
	@Transactional
	@DeleteMapping("/{idAssignment}")
	public ResponseEntity<Object> delete(@PathVariable Long idAssignment){
		cascadeDeleteService.deleteAssignmentAndAllRelatedEntities(idAssignment);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Assignment deleted successfully");
	}
	
	
	@Override
	@PutMapping("/{idAssignment}")
	public ResponseEntity<Object> update(@PathVariable Long idAssignment,
			@RequestBody @Valid AssignmentDto assignmentDto){
		assignmentService.replace(idAssignment, assignmentDto);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Assignment updated successfully");
	}
}
