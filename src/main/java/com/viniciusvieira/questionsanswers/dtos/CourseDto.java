package com.viniciusvieira.questionsanswers.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.viniciusvieira.questionsanswers.models.ProfessorModel;

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
public class CourseDto {
	@NotEmpty(message = "The course name cannot be empty")
	@Size(max = 150, message = "The course name accept max 150 caracteres")
	@Schema(description = "This is the course's name", required = true)
	private String name;
	
}
