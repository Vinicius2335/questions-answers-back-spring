package com.viniciusvieira.questionsanswers.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciusvieira.questionsanswers.dtos.ChoiceDto;
import com.viniciusvieira.questionsanswers.excepiton.ChoiceNotFoundException;
import com.viniciusvieira.questionsanswers.mappers.ChoiceMapper;
import com.viniciusvieira.questionsanswers.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.models.QuestionModel;
import com.viniciusvieira.questionsanswers.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.repositories.ChoiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChoiceService {
	private final ChoiceRepository choiceRepository;
	private final QuestionService questionService;
	private final ApplicationUserRepository applicationUserRepository;
	
	public List<ChoiceModel> listChoiceByQuestion(Long idQuestion){
		ProfessorModel professor = extractProfessorFromToken();
		QuestionModel question = questionService.findByIdOrThrowQuestionNotFoundException(idQuestion);
		
		return choiceRepository.listChoiceByQuestionId(question.getIdQuestion(), professor.getIdProfessor());
	}
	
	private ChoiceModel getChoiceByIdOrThrowChoiceNotFoundException(Long idChoice) {
		return choiceRepository.findOneChoice(idChoice, extractProfessorFromToken().getIdProfessor())
				.orElseThrow(() -> new ChoiceNotFoundException("Choice Not Found"));
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
		ChoiceModel choiceFound = getChoiceByIdOrThrowChoiceNotFoundException(idChoice);
		ChoiceModel choiceToUpdated = ChoiceMapper.INSTANCE.toChoiceModel(choiceDto);
		
		questionService.findByIdOrThrowQuestionNotFoundException(choiceToUpdated.getQuestion().getIdQuestion());
		
		choiceFound.setTitle(choiceToUpdated.getTitle());
		choiceFound.setCorrectAnswer(choiceToUpdated.isCorrectAnswer());
		choiceRepository.save(choiceFound);
		updateChangingOtherChoicesCorrectAnswerToFalse(choiceToUpdated);
	}
	
	@Transactional
	public void delete(Long idChoice) {
		ChoiceModel choiceFound = getChoiceByIdOrThrowChoiceNotFoundException(idChoice);
		questionService.findByIdOrThrowQuestionNotFoundException(choiceFound.getQuestion().getIdQuestion());
		choiceRepository.deleteById(choiceFound.getIdChoice(), extractProfessorFromToken().getIdProfessor());
	}
	
	private void updateChangingOtherChoicesCorrectAnswerToFalse(ChoiceModel choice) {
		if (choice.isCorrectAnswer()) {
			choiceRepository.updatedAllOtherChoicesCorrectAnswerToFalse(choice, choice.getQuestion(),
					choice.getProfessor().getIdProfessor());
		}
	}
	
	public ProfessorModel extractProfessorFromToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (String)authentication.getPrincipal();
		ApplicationUserModel user = applicationUserRepository.findByUsername(username);
		return user.getProfessor();
	}
	
	public void deleteAllChoicesRelatedToQuestion(Long idQuestion) {
		choiceRepository.deleteAllChoicesRelatedToQuestion(idQuestion,
				extractProfessorFromToken().getIdProfessor());
	}

}

// TODO: CASCATE DELETE