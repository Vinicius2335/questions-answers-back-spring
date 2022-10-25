package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.dtos.ChoiceDto;
import com.viniciusvieira.questionsanswers.models.ChoiceModel;

public class ChoiceCreator {
	public static ChoiceModel mockChoice() {
		return ChoiceModel.builder()
				.idChoice(1L)
				.title("Vinicius")
				.correctAnswer(true)
				.question(QuestionCreator.mockQuestion())
				.professor(ProfessorCreator.mockProfessor())
				.enabled(true)
				.build();
	}
	
	public static ChoiceDto mockChoiceDto() {
		return ChoiceDto.builder()
				.title("Vieira")
				.correctAnswer(true)
				.question(QuestionCreator.mockQuestion())
				.build();
	}
	
	public static ChoiceDto mockChoiceDtoInvalid() {
		return ChoiceDto.builder()
				.title(null)
				.correctAnswer(true)
				.question(QuestionCreator.mockQuestion())
				.build();
	}
}
