package com.viniciusvieira.questionsanswers.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciusvieira.questionsanswers.dtos.QuestionDto;
import com.viniciusvieira.questionsanswers.excepiton.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.mappers.QuestionMapper;
import com.viniciusvieira.questionsanswers.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.models.QuestionModel;
import com.viniciusvieira.questionsanswers.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.repositories.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepository;
	private final ApplicationUserRepository applicationUserRepository;
	
	public QuestionModel findByIdOrThrowQuestionNotFoundException(Long id) {
		Long idProfessor = extractProfessorFromToken().getIdProfessor();
		
		return questionRepository.findOneQuestion(id, idProfessor)
				.orElseThrow(() -> new QuestionNotFoundException("Question Not Found"));
	}
	
	public List<QuestionModel> findByCourseAndTitle(Long idCourse, String title, Long idProfessor){
		return questionRepository.listQuestionByCourseAndTitle(idCourse, title, idProfessor);
	}
	
	// BUG: E O COURSE ?
	@Transactional
	public QuestionModel save(QuestionDto questionDto, ProfessorModel professor) {
		QuestionModel question = QuestionMapper.INSTANCE.toQuestionModel(questionDto);
		question.setProfessor(professor);
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

}
