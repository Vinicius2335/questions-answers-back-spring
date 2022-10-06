package com.viniciusvieira.questionsanswers.endpoint.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusvieira.questionsanswers.dtos.CoursePostDto;
import com.viniciusvieira.questionsanswers.dtos.CoursePutDto;
import com.viniciusvieira.questionsanswers.excepiton.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.models.CourseModel;
import com.viniciusvieira.questionsanswers.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.services.CourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/professor/course")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class CourseEndPoint {
	private final CourseService courseService;
	
	@Operation(summary = "Find course by his Id", description = "Return a course based on it's id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "When Successful"),
			@ApiResponse(responseCode = "404", description = "When Course Not Found By ID")
	})
	@GetMapping("/{id}")
	public ResponseEntity<CourseModel> getCourseById(@PathVariable Long id) {
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(courseService.findByIdOrThrowCourseNotFoundException(id));
	}
	
	@Operation(summary = "Find courses by name", description = "Return a list of courses related to professor")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "When Successful"),
			@ApiResponse(responseCode = "404", description = "When Course List is Empty")
	})
	@GetMapping("/list")
	public ResponseEntity<List<CourseModel>> findByName(@RequestParam String name) {
		List<CourseModel> courses = courseService.findByName(name);
		
		if (courses.isEmpty()) {
			throw new CourseNotFoundException("Course List is Empty");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(courses);
	}
	
	@Operation(summary = "Save Course", description = "Insert course in the database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "When Successful"),
			@ApiResponse(responseCode = "400", description = "When Have a Course field Empty")
	})
	@PostMapping
	public ResponseEntity<CourseModel> save(@RequestBody @Valid CoursePostDto courseDto,
			Authentication authentication){
		ProfessorModel professor = courseService.extractProfessorFromToken();
		return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseDto, professor));
	}
	
	@Operation(summary = "Delete Course", description = "Remove course in the database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "404", description = "When Course Not Found")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id){
		courseService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Course deleted successfully");
	}
	
	@Operation(summary = "Update Couse", description = "Update course in the database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "When Successful"),
			@ApiResponse(responseCode = "404", description = "When Course Not Found")
	})
	@PutMapping("/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid CoursePutDto courseDto){
		courseService.replace(id, courseDto);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Course updated successfully");
	}
}

// 400 -> Bad Request
// 404 -> Not Found
