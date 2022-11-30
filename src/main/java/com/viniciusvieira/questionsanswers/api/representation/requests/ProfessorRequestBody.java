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
public class ProfessorRequestBody {
    @NotEmpty(message = "The professor name cannot be empty")
    private String name;

    @NotEmpty(message = "The professor email cannot be empty")
    private String email;
}
