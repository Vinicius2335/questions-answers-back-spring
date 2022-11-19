package com.viniciusvieira.questionsanswers.domain.excepiton;

public class ConflictException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ConflictException(String message) {
		super(message);
	}
	
}
