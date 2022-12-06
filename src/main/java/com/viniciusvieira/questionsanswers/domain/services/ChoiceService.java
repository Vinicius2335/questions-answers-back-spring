package com.viniciusvieira.questionsanswers.domain.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciusvieira.questionsanswers.api.mappers.v1.ChoiceMapper;
import com.viniciusvieira.questionsanswers.api.representation.models.ChoiceDto;
import com.viniciusvieira.questionsanswers.domain.exception.ChoiceNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.ChoiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChoiceService {
	private final ChoiceRepository choiceRepository;
	private final QuestionService questionService;
	private final CourseService courseService;
	private final QuestionAssignmentService questionAssignmentService;
	private final ApplicationUserRepository applicationUserRepository;
	
	public List<ChoiceModel> listChoiceByQuestion(Long idQuestion){
		ProfessorModel professor = extractProfessorFromToken();
		QuestionModel question = questionService.findByIdOrThrowQuestionNotFoundException(idQuestion);
		
		return choiceRepository.listChoiceByQuestionId(question.getIdQuestion(), professor.getIdProfessor());
	}
	
	public ChoiceModel getChoiceByIdOrThrowChoiceNotFoundException(Long idChoice) {
		return choiceRepository.findOneChoice(idChoice, extractProfessorFromToken().getIdProfessor())
				.orElseThrow(() -> new ChoiceNotFoundException("Choice Not Found"));
	}
	
	public List<ChoiceModel> findByQuestionAndTitle(Long idQuestion, String title){
		questionService.findByIdOrThrowQuestionNotFoundException(idQuestion);
		ProfessorModel professor = extractProfessorFromToken();
		
		return choiceRepository.listChoiceByQuestionAndTitle(idQuestion, title, professor.getIdProfessor());
	}
	
	@Transactional
	public ChoiceModel save(ChoiceDto choiceDto) {
		ChoiceModel choice = ChoiceMapper.INSTANCE.toChoiceModel(choiceDto);
		questionService.findByIdOrThrowQuestionNotFoundException(choice.getQuestion().getIdQuestion());
		ProfessorModel professor = extractProfessorFromToken();
		
		choice.setEnabled(true);
		choice.setProfessor(professor);
		ChoiceModel savedChoice = choiceRepository.save(choice);
		updateChangingOtherChoicesCorrectAnswerToFalse(choice);
		
		return savedChoice;
	}
	
	@Transactional
	public void replace(Long idChoice, ChoiceDto choiceDto) {
		ChoiceModel choiceToUpdate = getChoiceByIdOrThrowChoiceNotFoundException(idChoice);
		ChoiceModel newChoice = ChoiceMapper.INSTANCE.toChoiceModel(choiceDto);
		
		questionService.findByIdOrThrowQuestionNotFoundException(newChoice.getQuestion().getIdQuestion());
		
		choiceToUpdate.setTitle(newChoice.getTitle());
		choiceToUpdate.setCorrectAnswer(newChoice.isCorrectAnswer());
		choiceRepository.save(choiceToUpdate);
		updateChangingOtherChoicesCorrectAnswerToFalse(choiceToUpdate);
	}
	
	@Transactional
	public void delete(Long idChoice) {
		ChoiceModel choiceFound = getChoiceByIdOrThrowChoiceNotFoundException(idChoice);
		questionService.findByIdOrThrowQuestionNotFoundException(choiceFound.getQuestion().getIdQuestion());
		
		throwsConflictExceptionIfQuestionIsBeingUseInAnyAssignment(choiceFound.getQuestion().getIdQuestion());
		
		choiceRepository.deleteById(choiceFound.getIdChoice(), extractProfessorFromToken().getIdProfessor());
	}
	
	private void throwsConflictExceptionIfQuestionIsBeingUseInAnyAssignment(long questionId) {
		questionAssignmentService.throwsConflictExceptionIfQuestionIsBeingUseInAnyAssignment(questionId);
	}
	
	private void updateChangingOtherChoicesCorrectAnswerToFalse(ChoiceModel choice) {
		if (choice.isCorrectAnswer()) {
			choiceRepository.updatedAllOtherChoicesCorrectAnswerToFalse(choice, choice.getQuestion(),
					choice.getProfessor().getIdProfessor());
		}
	}

	// método mantido apenas como referencia
	public ProfessorModel extractProfessorFromToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (String)authentication.getPrincipal();
		ApplicationUserModel user = applicationUserRepository.findByUsername(username);
		return user.getProfessor();
	}
	
	public void deleteAllChoicesRelatedToQuestion(Long idQuestion) {
		questionService.findByIdOrThrowQuestionNotFoundException(idQuestion);
		choiceRepository.deleteAllChoicesRelatedToQuestion(idQuestion,
				extractProfessorFromToken().getIdProfessor());
	}
	
	public void deleteAllChoicesRelatedToCourse(Long idCourse) {
		courseService.findByIdOrThrowCourseNotFoundException(idCourse);
		choiceRepository.deleteAllChoicesRelatedToCourse(idCourse,
				extractProfessorFromToken().getIdProfessor());
	}

}

