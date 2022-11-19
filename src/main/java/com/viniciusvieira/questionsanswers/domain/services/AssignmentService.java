package com.viniciusvieira.questionsanswers.domain.services;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciusvieira.questionsanswers.api.mappers.AssignmentMapper;
import com.viniciusvieira.questionsanswers.api.representation.models.AssignmentDto;
import com.viniciusvieira.questionsanswers.domain.excepiton.AssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.AssignmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssignmentService {
	private final AssignmentRepository assignmentRepository;
	private final ApplicationUserRepository applicationUserRepository;
	private final CourseService courseService;
	
	public AssignmentModel findAssignmentOrThrowsAssignmentNotFoundException(Long idAssignment) {
		ProfessorModel professor = extractProfessorFromToken();
		return assignmentRepository.findOneAssignment(idAssignment, professor.getIdProfessor())
				.orElseThrow(() -> new AssignmentNotFoundException("Assignment Not Found"));
	}
	
	public List<AssignmentModel> findByCourseAndTitle(Long idCourse, String title){
		ProfessorModel professor = extractProfessorFromToken();
		CourseModel courseFound = courseService.findByIdOrThrowCourseNotFoundException(idCourse);
		
		return assignmentRepository.listAssignmentByCourseAndTitle(courseFound.getIdCourse(),
				title, professor.getIdProfessor());
	}
	
	//TEST
	@Transactional
	public AssignmentModel save(AssignmentDto assignmentDto) {
		courseService.findByIdOrThrowCourseNotFoundException(assignmentDto.getCourse().getIdCourse());
		AssignmentModel assignmentModel = AssignmentMapper.INSTANCE.toAssignmentModel(assignmentDto);
		ProfessorModel professor = extractProfessorFromToken();
		
		assignmentModel.setEnabled(true);
		assignmentModel.setProfessor(professor);
		assignmentModel.setAccessCode(generateAccessCode(assignmentModel.getCourse().getIdCourse()));
		
		return assignmentRepository.save(assignmentModel);
	}
	
	private Long generateAccessCode(Long courseId) {
		Long accessCode = ThreadLocalRandom.current().nextLong(1000, 10000);
		while(assignmentRepository.accessCodeExistsForCourse(accessCode, courseId) != null) {
			generateAccessCode(courseId);
		}
		
		return accessCode;
	}
	
	@Transactional
	public void replace(Long idAssignment, AssignmentDto assignmentDto) {
		courseService.findByIdOrThrowCourseNotFoundException(assignmentDto.getCourse().getIdCourse());
		AssignmentModel assignmentToUpdate = findAssignmentOrThrowsAssignmentNotFoundException(idAssignment);
		
		assignmentToUpdate.setTitle(assignmentDto.getTitle());
		assignmentRepository.save(assignmentToUpdate);
	}
	
	@Transactional
	public void delete(Long idAssignment) {
		findAssignmentOrThrowsAssignmentNotFoundException(idAssignment);
		
		assignmentRepository.deleteById(idAssignment);
	}
	
	public void deleteAllAssignmentRelatedToCourse(Long idCourse) {
		ProfessorModel professor = extractProfessorFromToken();
		assignmentRepository.deleteAllAssignmentRelatedToCourse(idCourse, professor.getIdProfessor());
	}
	
	public ProfessorModel extractProfessorFromToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (String)authentication.getPrincipal();
		ApplicationUserModel user = applicationUserRepository.findByUsername(username);
		return user.getProfessor();
	}
}
