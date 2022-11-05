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

import com.viniciusvieira.questionsanswers.api.representation.models.CourseDto;
import com.viniciusvieira.questionsanswers.domain.excepiton.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.services.CascadeDeleteService;
import com.viniciusvieira.questionsanswers.domain.services.CourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor/course")
@RequiredArgsConstructor
@Tag(name = "Course", description = "Operations related to professors course")
public class CourseController {
	private final CourseService courseService;
	private final CascadeDeleteService cascadeDeleteService;
	
	@Operation(summary = "Find course by his Id", description = "Return a course based on it's id",
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Course Not Found By ID")	
			})
	@GetMapping("/{id}")
	public ResponseEntity<CourseModel> getCourseById(@PathVariable Long id) {
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(courseService.findByIdOrThrowCourseNotFoundException(id));
	}
	
	@Operation(summary = "Find courses by name", description = "Return a list of courses related to professor",
			responses = {
					@ApiResponse(responseCode = "200", description = "When Successful"),
					@ApiResponse(responseCode = "404", description = "When Course List is Empty")	
			})
	@GetMapping("/list")
	public ResponseEntity<List<CourseModel>> findByName(@RequestParam String name) {
		Long idProfessor = courseService.extractProfessorFromToken().getIdProfessor();
		List<CourseModel> courses = courseService.findByName(name, idProfessor);
		
		if (courses.isEmpty()) {
			throw new CourseNotFoundException("Course List is Empty");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(courses);
	}
	
	@Operation(summary = "Save Course", description = "Insert course in the database", responses = {
			@ApiResponse(responseCode = "201", description = "When Successful"),
			@ApiResponse(responseCode = "400", description = "When Have a Course field Empty")
	})
	@PostMapping
	public ResponseEntity<CourseModel> save(@RequestBody @Valid CourseDto courseDto){
		ProfessorModel professor = courseService.extractProfessorFromToken();
		return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseDto, professor));
	}
	
	@Operation(summary = "Delete Course", description = "Remove course in the database and all related questions"
			+ " and choices", responses = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "404", description = "When Course Not Found")
	})
	@DeleteMapping("/{id}")
	// Transactional porque realizaremos 2 alteraçoes no banco de dados, se desse erro em 1,
	// a outra funcionaria normalmente, porem só queremos salvar a alteraçao caso as 2 operaçoes aconteça
	@Transactional 
	public ResponseEntity<Object> delete(@PathVariable Long id){
		cascadeDeleteService.cascadeDeleteCourseQuestionAndChoice(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Course deleted successfully");
	}
	
	@Operation(summary = "Update Couse", description = "Update course in the database", responses = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "400", description = "When Course Name is Null or Empty"),
			@ApiResponse(responseCode = "404", description = "When Course Not Found")
	})
	@PutMapping("/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid CourseDto courseDto){
		courseService.replace(id, courseDto);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Course updated successfully");
	}
}

// 400 -> Bad Request
// 404 -> Not Found
