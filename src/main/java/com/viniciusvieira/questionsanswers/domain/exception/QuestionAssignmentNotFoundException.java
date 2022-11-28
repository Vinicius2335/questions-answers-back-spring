package com.viniciusvieira.questionsanswers.domain.exception;


public class QuestionAssignmentNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public QuestionAssignmentNotFoundException(String message) {
		super(message);
	}
}
