package com.viniciusvieira.questionsanswers.api.representation.models;

import javax.validation.constraints.NotNull;

import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionAssignmentDto {
	@NotNull(message = "The QuestionAssignment grade cannot be null")
	@Schema(description = "This is the questionAssignment grade", required = true)
	private double grade;
	
	@NotNull(message = "The QuestionAssignment field question cannot be null")
	@Schema(description = "This is the questionAssignment question", required = true)
	private QuestionModel question;
	
	@NotNull(message = "The QuestionAssignment field assignment cannot be null")
	@Schema(description = "This is the questionAssignment assignment", required = true)
	private AssignmentModel assignment;
}
