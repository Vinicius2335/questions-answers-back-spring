package com.viniciusvieira.questionsanswers.domain.exception;

public class QuestionAssignmetAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public QuestionAssignmetAlreadyExistsException(String message) {
		super(message);
	}
}
