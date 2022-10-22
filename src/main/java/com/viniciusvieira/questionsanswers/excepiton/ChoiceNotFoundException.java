package com.viniciusvieira.questionsanswers.excepiton;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChoiceNotFoundException extends RuntimeException {

	public ChoiceNotFoundException(String message) {
		super(message);
	}
	
}
