package com.viniciusvieira.questionsanswers.api.representation.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.viniciusvieira.questionsanswers.domain.models.CourseModel;

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
	
	@NotNull(message = "The question course cannot be null")
	@Schema(description = "This is the question's course", required = true)
	private CourseModel course;
	
}
