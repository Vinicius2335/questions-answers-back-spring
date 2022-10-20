package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.dtos.QuestionDto;
import com.viniciusvieira.questionsanswers.models.QuestionModel;

public class QuestionCreator {
	public static QuestionModel mockQuestion() {
		return QuestionModel.builder()
				.idQuestion(1L)
				.title("Qual é seu nome?")
				.course(CourseCreator.mockCourse())
				.professor(ProfessorCreator.mockProfessor())
				.build();
	}
	
	public static QuestionDto mockQuestionDto() {
		return QuestionDto.builder()
				.title("Qual é seu nome?")
				.build();
	}
	
	public static QuestionDto mockQuestionDtoInvalid() {
		return QuestionDto.builder()
				.title(null)
				.build();
	}
}
