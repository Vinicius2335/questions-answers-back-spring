package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.dtos.CourseDto;
import com.viniciusvieira.questionsanswers.models.CourseModel;

public class CourseCreator {
	
	public static CourseModel mockCourse() {
		return CourseModel.builder()
				.idCourse(1L)
				.name("Java")
				.professor(ProfessorCreator.mockProfessor())
				.enabled(true)
				.build();
	}
	
	public static CourseModel mockCourseToSave() {
		return CourseModel.builder()
				.idCourse(2L)
				.name("CSS")
				.professor(ProfessorCreator.mockProfessor())
				.enabled(true)
				.build();
	}
	
	public static CourseModel mockCourseUpdated() {
		return CourseModel.builder()
				.idCourse(1L)
				.name("Python")
				.professor(ProfessorCreator.mockProfessor())
				.enabled(true)
				.build();
	}
	
	public static CourseDto mockCourseDto() {
		return CourseDto.builder()
				.name("CSS")
				.build();
	}
	
	public static CourseDto mockCourseDtoToUpdated() {
		return CourseDto.builder()
				.name("Python")
				.build();
	}
	
	
	public static CourseDto mockInvalidCourseDto() {
		return CourseDto.builder()
				.name(null)
				.build();
	}
	
}
