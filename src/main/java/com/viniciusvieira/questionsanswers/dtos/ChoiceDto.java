package com.viniciusvieira.questionsanswers.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.viniciusvieira.questionsanswers.models.QuestionModel;

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
public class ChoiceDto {
	@NotEmpty(message = "The title choice cannot be empty")
	@Schema(description = "The title of choice", required = true)
	private String title;
	
	@NotNull
	@Schema(description = "The question related to choice")
	private QuestionModel question;
	
	// BUG: ficar esperto com o erro de constraint aki
	@NotNull
	@Schema(description = "Correct answer for the associated question, you can have only one correct answer per question")
	private boolean correctAnswer;
}
