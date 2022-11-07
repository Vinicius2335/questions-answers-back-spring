package com.viniciusvieira.questionsanswers.domain.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.viniciusvieira.questionsanswers.domain.excepiton.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.domain.excepiton.QuestionNotFoundException;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for cascade delete service")
class CascadeDeleteServiceTest {
	@InjectMocks
	private CascadeDeleteService cascadeDeleteService;
	
	@Mock
	private QuestionService questionServiceMock;
	@Mock
	private ChoiceService choiceServiceMock;
	@Mock
	private CourseService courseServiceMock;
	@Mock
	private AssignmentService assignmentService;
	
	@BeforeEach
	void setUp() throws Exception {
		
		// course service delete
		BDDMockito.doNothing().when(courseServiceMock).delete(anyLong());
		
		// question service deleteAllQuestionsRelatedToCouse
		BDDMockito.doNothing().when(questionServiceMock).deleteAllQuestionsRelatedToCouse(anyLong());
		
		// choice service deleteAllChoicesRelatedToCourse
		BDDMockito.doNothing().when(choiceServiceMock).deleteAllChoicesRelatedToCourse(anyLong());
		
		// assignment service deleteAllAssignmentRelatedToCourse
		BDDMockito.doNothing().when(assignmentService).deleteAllAssignmentRelatedToCourse(anyLong());
		
		// question service delete
		BDDMockito.doNothing().when(questionServiceMock).delete(anyLong());
		
		// choice service deleteAllChoicesRelatedToQuestion
		BDDMockito.doNothing().when(choiceServiceMock).deleteAllChoicesRelatedToQuestion(anyLong());
	}

	@Test
	@DisplayName("cascadeDeleteCourseQuestionAndChoice remove course and all related questions, choices and assignments")
	void cascadeDeleteCourseQuestionAndChoice_RemoveCourseAndAllRelatedQuestionsChoicesAndAssignment_WhenSuccessful() {
		assertDoesNotThrow(() -> cascadeDeleteService.deleteCourseAndAllRelatedEntities(1L));
	}
	
	@Test
	@DisplayName("cascadeDeleteCourseQuestionAndChoice Throws CourseNotFoundException When course not found by id")
	void cascadeDeleteCourseQuestionAndChoice_ThrowsCourseNotFoundException_WhenCourseNotFound() {
		BDDMockito.doThrow(CourseNotFoundException.class).when(courseServiceMock).delete(anyLong());
		
		assertThrows(CourseNotFoundException.class, () -> cascadeDeleteService
				.deleteCourseAndAllRelatedEntities(1L));
	}
	
	@Test
	@DisplayName("cascadeDeleteQuestionAndChoice remove question and all related choices")
	void cascadeDeleteQuestionAndChoice_RemoveQuestionAndAllRelatedChoices_WhenSuccessful() {
		assertDoesNotThrow(() -> cascadeDeleteService.deleteQuestionAndAllRelatedEntities(1L));
	}
	
	@Test
	@DisplayName("cascadeDeleteQuestionAndChoice Throws QuestionNotFoundException When question not found by id")
	void cascadeDeleteQuestionAndChoice_ThrowsQuestionNotFoundException_WhenQuestionNotFound() {
		BDDMockito.doThrow(QuestionNotFoundException.class).when(questionServiceMock).delete(anyLong());
		
		assertThrows(QuestionNotFoundException.class, () -> cascadeDeleteService.deleteQuestionAndAllRelatedEntities(1L));
	}

}
