package com.viniciusvieira.questionsanswers.dtos;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class QuestionDto {
	@NotEmpty(message = "The question title cannot be empty")
	@Schema(description = "This is the question's title", required = true)
	private String title;
}
