package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.api.representation.models.AssignmentDto;
import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;

public class AssignmentCreator {
	
	public static AssignmentModel mockAssignment() {
		return AssignmentModel.builder()
				.idAssignment(1L)
				.title("teste assignment")
				.course(CourseCreator.mockCourse())
				.professor(ProfessorCreator.mockProfessor())
				.accessCode("1234")
				.enabled(true)
				.build();
	}
	
	
	public static AssignmentDto mockAssignmentDto() {
		return AssignmentDto.builder()
				.title("teste assignment")
				.course(CourseCreator.mockCourse())
				.build();
	}
	
	public static AssignmentDto mockAssignmentDtoToUpdate() {
		return AssignmentDto.builder()
				.title("teste assignment update")
				.course(CourseCreator.mockCourse())
				.build();
	}
	
	public static AssignmentDto mockAssignmentDtoInvalid() {
		return AssignmentDto.builder()
				.title(null)
				.course(null)
				.build();
	}
	
}
