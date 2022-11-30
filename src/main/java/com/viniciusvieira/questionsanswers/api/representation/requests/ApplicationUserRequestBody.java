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
public class ApplicationUserRequestBody {
    @NotEmpty(message = "The applicationUser username cannot be empty")
    private String username;
    @NotEmpty(message = "The applicationUser password cannot be empty")
    private String password;
}
