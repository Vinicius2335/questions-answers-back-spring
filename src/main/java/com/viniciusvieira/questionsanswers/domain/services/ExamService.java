package com.viniciusvieira.questionsanswers.domain.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.viniciusvieira.questionsanswers.domain.excepiton.ChoiceNotFoundException;
import com.viniciusvieira.questionsanswers.domain.excepiton.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionAssignmentModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ChoiceRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.QuestionAssignmentRepository;

import lombok.RequiredArgsConstructor;

// TEST
@Service
@RequiredArgsConstructor
public class ExamService {
	private final QuestionAssignmentRepository questionAssignmentRepository;
	private final ChoiceRepository choiceRepository;
	
	public List<QuestionModel> findAllQuestionsByAccessCode(String accessCode){
		List<QuestionAssignmentModel> questionAssignments = questionAssignmentRepository
				.listQuestionsFromQuestionsAssignmentByAssignmentAccessCode(accessCode);
		
		List<QuestionModel> questions = questionAssignments.stream()
				.map(QuestionAssignmentModel::getQuestion).toList();
		
		if (questions.isEmpty()) {
			throw new QuestionNotFoundException("Question not found");
		}
		
		return questions;
	}
	
	public List<ChoiceModel> findAllChoicessByAccessCode(String accessCode){
		List<QuestionAssignmentModel> questionAssignments = questionAssignmentRepository
				.listQuestionsFromQuestionsAssignmentByAssignmentAccessCode(accessCode);
		
		List<QuestionModel> questions = questionAssignments.stream()
				.map(QuestionAssignmentModel::getQuestion).toList();
		
		List<Long> questionsId = questions.stream().map(QuestionModel::getIdQuestion).toList();
		
		List<ChoiceModel> choices = choiceRepository.listChoiceByQuestionsIdForStudent(questionsId);
		
		if (choices.isEmpty()) {
			throw new ChoiceNotFoundException("Choice not found");
		}
		
		return choices;
	}
}
