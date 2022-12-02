package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.domain.models.StudentModel;

public abstract class StudentCreator {
    public static StudentModel mockStudent(){
        return StudentModel.builder()
                .idStudent(1L)
                .email("student@email.com")
                .enabled(true)
                .name("student")
                .build();
    }
}
