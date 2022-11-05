package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.api.representation.models.QuestionDto;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;

public class QuestionCreator {
	public static QuestionModel mockQuestion() {
		return QuestionModel.builder()
				.idQuestion(1L)
				.title("Qual é seu nome?")
				.course(CourseCreator.mockCourse())
				.professor(ProfessorCreator.mockProfessor())
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
