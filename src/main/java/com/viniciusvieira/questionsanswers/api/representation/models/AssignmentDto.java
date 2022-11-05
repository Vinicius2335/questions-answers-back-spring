package com.viniciusvieira.questionsanswers.api.representation.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.viniciusvieira.questionsanswers.domain.models.CourseModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentDto {
	@NotBlank(message = "The title assignment cannot be empty or null")
	@Schema(description = "The title of assignment", required = true)
	private String title;
	
	@NotNull(message = "The course assignment cannot be null")
	@Schema(description = "The course of assignment", required = true)
	private CourseModel course;
}
