package com.viniciusvieira.questionsanswers.domain.excepiton;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChoiceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ChoiceNotFoundException(String message) {
		super(message);
	}
	
}
