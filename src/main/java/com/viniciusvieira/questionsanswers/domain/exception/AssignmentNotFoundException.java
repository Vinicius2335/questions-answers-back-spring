package com.viniciusvieira.questionsanswers.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AssignmentNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AssignmentNotFoundException(String message) {
		super(message);
	}

}

