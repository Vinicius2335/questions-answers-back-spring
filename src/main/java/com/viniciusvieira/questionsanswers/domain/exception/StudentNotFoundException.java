package com.viniciusvieira.questionsanswers.domain.exception;

import java.io.Serial;

public class StudentNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    public StudentNotFoundException(String message) {
        super(message);
    }
}
