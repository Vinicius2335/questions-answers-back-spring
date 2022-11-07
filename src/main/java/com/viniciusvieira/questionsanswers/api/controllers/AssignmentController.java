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

import com.viniciusvieira.questionsanswers.api.representation.models.AssignmentDto;
import com.viniciusvieira.questionsanswers.domain.excepiton.AssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;
import com.viniciusvieira.questionsanswers.domain.services.AssignmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor/course/assignment")
@RequiredArgsConstructor
@Tag(name = "Assignment", description = "Operations related to course's assignment")
public class AssignmentController {
	private final AssignmentService assignmentService;
	
	@Operation(summary = "Find assignment by his Id" , description = "Return a assignment based on it's id", 
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Assignment Not Found By ID")
			})
	@GetMapping("/{idAssignment}")
	public ResponseEntity<AssignmentModel> getAssignmentById(@PathVariable Long idAssignment){
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(assignmentService.findAssignmentOrThrowsAssignmentNotFoundException(idAssignment));
	}
	
	
	@Operation(summary = "Find assignment by title", description = "Return a list of assignments related to id coursen and professor",
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Course Not Found"),
					@ApiResponse(responseCode = "404", description = "When Assignment List is Empty")	
			})
	@GetMapping("/list/{idCourse}/")
	// /api/professor/course/assignement/1/?title=" "
	public ResponseEntity<List<AssignmentModel>> findByCourseAndTitle(@PathVariable Long idCourse ,
			@RequestParam String title){
		List<AssignmentModel> listAssignment = assignmentService
				.findByCourseAndTitle(idCourse, title);
		
		if (listAssignment.isEmpty()) {
			throw new AssignmentNotFoundException("Assignment List is Empty");
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(listAssignment);
	}
	
	
	@Operation(summary = "Save Assignment", description = "Insert assignment in the database", responses = {
			@ApiResponse(responseCode = "201", description = "When Successful"),
			@ApiResponse(responseCode = "404", description = "When Course Not Found"),
			@ApiResponse(responseCode = "400", description = "When Have a Assignment field Empty or Null")
	})
	@PostMapping
	public ResponseEntity<AssignmentModel> save(@RequestBody @Valid AssignmentDto assignmentDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(assignmentService.save(assignmentDto));
	}
	
	
	@Operation(summary = "Delete Assignment", description = "Remove assignment in the database", responses = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "404", description = "When Assignment Not Found")
	})
	@Transactional
	@DeleteMapping("/{idAssignment}")
	public ResponseEntity<Object> delete(@PathVariable Long idAssignment){
		assignmentService.delete(idAssignment);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Assignment deleted successfully");
	}
	
	
	@Operation(summary = "Update Assignment", description = "Update assignment in the database", responses = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "400", description = "When Assignment Title is Null or Empty"),
			@ApiResponse(responseCode = "404", description = "When Course Not Found"),
			@ApiResponse(responseCode = "404", description = "When Assignment Not Found")
	})
	@PutMapping("/{idAssignment}")
	public ResponseEntity<Object> update(@PathVariable Long idAssignment, @RequestBody @Valid AssignmentDto assignmentDto){
		assignmentService.replace(idAssignment, assignmentDto);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Assignment updated successfully");
	}
}
