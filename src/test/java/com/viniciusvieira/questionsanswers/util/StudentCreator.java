package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.api.representation.models.StudentDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.StudentRequestBody;
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

    public static StudentDto mockStudentDto(){
        return StudentDto.builder()
                .idStudent(1L)
                .email("student@email.com")
                .name("student")
                .build();
    }

    public static StudentRequestBody mockStudentRequestBody(){
        return StudentRequestBody.builder()
                .name("student")
                .email("student@email.com")
                .build();
    }
}
