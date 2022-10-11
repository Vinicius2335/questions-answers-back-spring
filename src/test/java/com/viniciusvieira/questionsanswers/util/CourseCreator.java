package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.dtos.CoursePostDto;
import com.viniciusvieira.questionsanswers.models.CourseModel;

public class CourseCreator {
	
	public static CourseModel mockCourse() {
		return CourseModel.builder()
				.idCourse(1L)
				.name("Java")
				.professor(ProfessorCreator.mockProfessor()).build();
	}
	
	public static CourseModel mockCourseToSave() {
		return CourseModel.builder()
				.idCourse(2L)
				.name("CSS")
				.professor(ProfessorCreator.mockProfessor())
				.build();
	}
	
	public static CoursePostDto mockCoursePostDto() {
		return CoursePostDto.builder()
				.name("CSS")
				.build();
	}
	
}
