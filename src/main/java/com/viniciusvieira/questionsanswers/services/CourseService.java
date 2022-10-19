package com.viniciusvieira.questionsanswers.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciusvieira.questionsanswers.dtos.CourseDto;
import com.viniciusvieira.questionsanswers.excepiton.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.mappers.CourseMapper;
import com.viniciusvieira.questionsanswers.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.models.CourseModel;
import com.viniciusvieira.questionsanswers.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.repositories.CourseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {
	private final CourseRepository courseRepository;
	private final ApplicationUserRepository applicationUserRepository;
	
	public CourseModel findByIdOrThrowCourseNotFoundException(Long id) {
		Long idProfessor = extractProfessorFromToken().getIdProfessor();
		
		return courseRepository.findOneCourse(id, idProfessor)
				.orElseThrow(() -> new CourseNotFoundException("Course Not Found"));
	}
	
	public List<CourseModel> findByName(String name, Long idProfessor){
		return courseRepository.listCoursesByName(name, idProfessor);
	}
	
	@Transactional
	public CourseModel save(CourseDto courseDto, ProfessorModel professor) {
		CourseModel course = CourseMapper.INSTANCE.toCorseModel(courseDto);
		course.setProfessor(professor);
		course.setEnabled(true);
		return courseRepository.save(course);
	}
	
	@Transactional
	public void replace(Long id, CourseDto courseDto) {
		CourseModel course = CourseMapper.INSTANCE.toCorseModel(courseDto);
		CourseModel courseFound = findByIdOrThrowCourseNotFoundException(id);
		
		courseFound.setName(course.getName());
		courseRepository.save(courseFound);
	}
	
	@Transactional
	public void delete(Long id) {
		CourseModel course = findByIdOrThrowCourseNotFoundException(id);
		ProfessorModel professor = extractProfessorFromToken();
		
		courseRepository.deleteById(course.getIdCourse(), professor.getIdProfessor());
	}
	
	public ProfessorModel extractProfessorFromToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (String)authentication.getPrincipal();
		ApplicationUserModel user = applicationUserRepository.findByUsername(username);
		return user.getProfessor();
	}

}
