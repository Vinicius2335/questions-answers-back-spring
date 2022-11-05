package com.viniciusvieira.questionsanswers.domain.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciusvieira.questionsanswers.api.mappers.QuestionMapper;
import com.viniciusvieira.questionsanswers.api.representation.models.QuestionDto;
import com.viniciusvieira.questionsanswers.domain.excepiton.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepository;
	private final ApplicationUserRepository applicationUserRepository;
	private final CourseService courseService;
	
	public QuestionModel findByIdOrThrowQuestionNotFoundException(Long id) {
		Long idProfessor = extractProfessorFromToken().getIdProfessor();
		
		return questionRepository.findOneQuestion(id, idProfessor)
				.orElseThrow(() -> new QuestionNotFoundException("Question Not Found"));
	}
	
	public List<QuestionModel> findByCourseAndTitle(Long idCourse, String title, Long idProfessor){
		return questionRepository.listQuestionByCourseAndTitle(idCourse, title, idProfessor);
	}
	
	@Transactional
	public QuestionModel save(QuestionDto questionDto) {
		QuestionModel question = QuestionMapper.INSTANCE.toQuestionModel(questionDto);
		CourseModel course = courseService.findByIdOrThrowCourseNotFoundException(question.getCourse().getIdCourse());
		
		question.setProfessor(course.getProfessor());
		question.setEnabled(true);
		return questionRepository.save(question);
	}
	
	@Transactional
	public void replace(Long id, QuestionDto questionDto) {
		QuestionModel question = QuestionMapper.INSTANCE.toQuestionModel(questionDto);
		QuestionModel questionFound = findByIdOrThrowQuestionNotFoundException(id);
		
		questionFound.setTitle(question.getTitle());
		questionRepository.save(questionFound);
	}
	
	@Transactional
	public void delete(Long id) {
		QuestionModel question = findByIdOrThrowQuestionNotFoundException(id);
		ProfessorModel professor = extractProfessorFromToken();
		
		questionRepository.deleteById(question.getIdQuestion(), professor.getIdProfessor());
	}
	
	public ProfessorModel extractProfessorFromToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (String)authentication.getPrincipal();
		ApplicationUserModel user = applicationUserRepository.findByUsername(username);
		return user.getProfessor();
	}
	
	@Transactional
	public void deleteAllQuestionsRelatedToCouse(Long idCourse) {
		ProfessorModel professor = extractProfessorFromToken();
		questionRepository.deleteAllQuestionsRelatedToCouse(idCourse, professor.getIdProfessor());
	}

}
