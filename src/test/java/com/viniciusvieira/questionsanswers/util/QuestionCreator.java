package com.viniciusvieira.questionsanswers.util;

import java.util.List;

import com.viniciusvieira.questionsanswers.api.representation.models.QuestionDto;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;

public abstract class QuestionCreator {
	public static QuestionModel mockQuestion() {
		return QuestionModel.builder()
				.idQuestion(1L)
				.title("Qual é seu nome?")
				.course(CourseCreator.mockCourse())
				.professor(ProfessorCreator.mockProfessor())
				.enabled(true)
				.build();
	}
	
	public static QuestionModel mockQuestion2Choices() {
		ChoiceModel choice2 = ChoiceModel.builder().idChoice(2L).title("Churrasco").correctAnswer(true)
				.question(QuestionCreator.mockQuestion()).professor(ProfessorCreator.mockProfessor()).enabled(true)
				.build();

		ChoiceModel choice3 = ChoiceModel.builder().idChoice(3L).title("Lasanha").correctAnswer(false)
				.question(QuestionCreator.mockQuestion()).professor(ProfessorCreator.mockProfessor()).enabled(true)
				.build();
		
		return QuestionModel.builder()
				.idQuestion(1L)
				.title("Qual é seu nome?")
				.course(CourseCreator.mockCourse())
				.professor(ProfessorCreator.mockProfessor())
				.choices(List.of(choice2, choice3))
				.enabled(true)
				.build();
	}
	
	
	public static QuestionDto mockQuestionDto() {
		return QuestionDto.builder()
				.title("Qual é seu nome?")
				.course(CourseCreator.mockCourse())
				.build();
	}
	
	
	public static QuestionDto mockQuestionDtoInvalid() {
		return QuestionDto.builder()
				.title(null)
				.course(CourseCreator.mockCourse())
				.build();
	}
}
