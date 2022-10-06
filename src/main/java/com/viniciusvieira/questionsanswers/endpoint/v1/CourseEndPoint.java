package com.viniciusvieira.questionsanswers.endpoint.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viniciusvieira.questionsanswers.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.models.CourseModel;
import com.viniciusvieira.questionsanswers.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.repositories.CourseRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/professor/course")
@RequiredArgsConstructor
public class CourseEndPoint {
	private final CourseRepository courseRepository;
	
	@GetMapping("/{id}")
	@Operation(summary = "Find course by his Id", description = "Return a course based on it's id")
	public ResponseEntity<CourseModel> getCourseById(@PathVariable Long id, Authentication authentication) {
		ProfessorModel professor = ((ApplicationUserModel)authentication.getPrincipal()).getProfessor();
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(courseRepository.findByProfessor(professor)
						.orElseThrow(() -> new IllegalArgumentException("Course id invalid")));
	}
}
