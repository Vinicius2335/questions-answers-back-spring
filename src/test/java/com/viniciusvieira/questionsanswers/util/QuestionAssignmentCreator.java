package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.api.representation.models.QuestionAssignmentDto;
import com.viniciusvieira.questionsanswers.domain.models.QuestionAssignmentModel;

public abstract class QuestionAssignmentCreator {
	
	public static QuestionAssignmentModel mockQuestionAssignment() {
		return QuestionAssignmentModel.builder()
				.idQuestionAssignment(1L)
				.question(QuestionCreator.mockQuestion())
				.assignment(AssignmentCreator.mockAssignment())
				.professor(ProfessorCreator.mockProfessor())
				.grade(100)
				.enabled(true).build();
	}
	
	public static QuestionAssignmentDto mockQuestionAssignmentDto() {
		return QuestionAssignmentDto.builder()
				.question(QuestionCreator.mockQuestion())
				.assignment(AssignmentCreator.mockAssignment())
				.grade(100).build();
	}
	
	public static QuestionAssignmentDto mockQuestionAssignmentDtoInvalid() {
		return QuestionAssignmentDto.builder()
				.question(null)
				.assignment(null)
				.grade(100).build();
	}
}
