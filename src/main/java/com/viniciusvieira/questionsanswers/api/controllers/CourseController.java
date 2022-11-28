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

import com.viniciusvieira.questionsanswers.api.openapi.controller.CourseControllerOpenApi;
import com.viniciusvieira.questionsanswers.api.representation.models.CourseDto;
import com.viniciusvieira.questionsanswers.domain.exception.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;
import com.viniciusvieira.questionsanswers.domain.services.CascadeDeleteService;
import com.viniciusvieira.questionsanswers.domain.services.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/professor/course")
@RequiredArgsConstructor
public class CourseController implements CourseControllerOpenApi {
	private final CourseService courseService;
	private final CascadeDeleteService cascadeDeleteService;
	
	@Override
	@GetMapping("/{idCourse}")
	public ResponseEntity<CourseModel> getCourseById(@PathVariable Long idCourse) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(courseService.findByIdOrThrowCourseNotFoundException(idCourse));
	}
	
	
	@Override
	@GetMapping("/list")
	public ResponseEntity<List<CourseModel>> findByName(@RequestParam(required = false, defaultValue = "")
			String name) {
		List<CourseModel> courses = courseService.findByName(name);
		
		if (courses.isEmpty()) {
			throw new CourseNotFoundException("Course List is Empty");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(courses);
	}
	
	
	@Override
	@PostMapping
	public ResponseEntity<CourseModel> save(@RequestBody @Valid CourseDto courseDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseDto));
	}
	
	
	@Override
	@DeleteMapping("/{idCourse}")
	// Transactional porque realizaremos 2 alteraçoes no banco de dados, se desse erro em 1,
	// a outra funcionaria normalmente, porem só queremos salvar a alteraçao caso as 2 operaçoes aconteça
	@Transactional 
	public ResponseEntity<Object> delete(@PathVariable Long idCourse){
		cascadeDeleteService.deleteCourseAndAllRelatedEntities(idCourse);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Course deleted successfully");
	}
	
	
	@Override
	@PutMapping("/{idCourse}")
	public ResponseEntity<Object> update(@PathVariable Long idCourse, @RequestBody @Valid CourseDto courseDto){
		courseService.replace(idCourse, courseDto);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Course updated successfully");
	}
}
