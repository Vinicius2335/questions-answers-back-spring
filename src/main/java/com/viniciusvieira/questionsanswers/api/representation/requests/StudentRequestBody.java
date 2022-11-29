package com.viniciusvieira.questionsanswers.api.representation.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentRequestBody {
    @NotEmpty(message = "The student name cannot be empty")
    private String name;

    @NotEmpty(message = "The student email cannot be empty")
    private String email;
}

