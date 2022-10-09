package com.viniciusvieira.questionsanswers.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciusvieira.questionsanswers.dtos.CoursePostDto;
import com.viniciusvieira.questionsanswers.dtos.CoursePutDto;
import com.viniciusvieira.questionsanswers.excepiton.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.mappers.CourseMapper;
import com.viniciusvieira.questionsanswers.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.models.CourseModel;
import com.viniciusvieira.questionsanswers.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.repositories.CourseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {
	private final CourseRepository courseRepository;
	
	public CourseModel findByIdOrThrowCourseNotFoundException(Long id) {
		return courseRepository.findById(id)
				.orElseThrow(() -> new CourseNotFoundException("Course Not Found"));
	}
	
	public List<CourseModel> findByName(String name, Long idProfessor){
		return courseRepository.listCoursesByName(name, idProfessor);
	}
	
	@Transactional
	public CourseModel save(CoursePostDto courseDto, ProfessorModel professor) {
		CourseModel course = CourseMapper.INSTANCE.toCorseModel(courseDto);
		course.setProfessor(professor);
		return courseRepository.save(course);
	}
	
	@Transactional
	public void replace(Long id, CoursePutDto courseDto) {
		CourseModel course = CourseMapper.INSTANCE.toCorseModel(courseDto);
		CourseModel courseFound = findByIdOrThrowCourseNotFoundException(id);
		
		course.setIdCourse(courseFound.getIdCourse());
		courseRepository.save(course);
	}
	
	@Transactional
	public void delete(Long id) {
		courseRepository.delete(findByIdOrThrowCourseNotFoundException(id));
	}
	
	public ProfessorModel extractProfessorFromToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((ApplicationUserModel)authentication.getPrincipal()).getProfessor();
	}

}
