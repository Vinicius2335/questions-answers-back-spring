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

import com.viniciusvieira.questionsanswers.domain.exception.AssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionAssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionNotFoundException;

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
	private AssignmentService assignmentServiceMock;
	@Mock
	private QuestionAssignmentService questionAssignmentServiceMock;
	
	@BeforeEach
	void setUp() throws Exception {
		
		// course service delete
		BDDMockito.doNothing().when(courseServiceMock).delete(anyLong());
		
		// assignment service delete
		BDDMockito.doNothing().when(assignmentServiceMock).delete(anyLong());
		
		// assignment service deleteAllAssignmentRelatedToCourse
		BDDMockito.doNothing().when(assignmentServiceMock).deleteAllAssignmentRelatedToCourse(anyLong());
		
		// question service delete
		BDDMockito.doNothing().when(questionServiceMock).delete(anyLong());
		
		// question service deleteAllQuestionsRelatedToCouse
		BDDMockito.doNothing().when(questionServiceMock).deleteAllQuestionsRelatedToCouse(anyLong());
		
		// choice service deleteAllChoicesRelatedToQuestion
		BDDMockito.doNothing().when(choiceServiceMock).deleteAllChoicesRelatedToQuestion(anyLong());
		
		// choice service deleteAllChoicesRelatedToCourse
		BDDMockito.doNothing().when(choiceServiceMock).deleteAllChoicesRelatedToCourse(anyLong());
		
		// questionAssignment service deleteAllQuestionAssignmentRelatedToCourse
		BDDMockito.doNothing().when(questionAssignmentServiceMock).deleteAllQuestionAssignmentRelatedToCourse(anyLong());
		
		// questionAssignment service deleteAllQuestionAssignmentRelatedToAssignment
		BDDMockito.doNothing().when(questionAssignmentServiceMock).deleteAllQuestionAssignmentRelatedToAssignment(anyLong());

		// questionAssignment service deleteAllQuestionAssignmentRelatedToQuestion
		BDDMockito.doNothing().when(questionAssignmentServiceMock).deleteAllQuestionAssignmentRelatedToQuestion(anyLong());
		
		// questionAssignment service delete
		BDDMockito.doNothing().when(questionAssignmentServiceMock).delete(anyLong());
	}

	@Test
	@DisplayName("deleteCourseAndAllRelatedEntities remove course and all related entities")
	void deleteCourseAndAllRelatedEntities_RemoveCourseAndAllRelatedentities_WhenSuccessful() {
		assertDoesNotThrow(() -> cascadeDeleteService.deleteCourseAndAllRelatedEntities(1L));
	}
	
	@Test
	@DisplayName("deleteCourseAndAllRelatedEntities Throws CourseNotFoundException When course not found by id")
	void deleteCourseAndAllRelatedEntities_ThrowsCourseNotFoundException_WhenCourseNotFound() {
		BDDMockito.doThrow(CourseNotFoundException.class).when(courseServiceMock).delete(anyLong());
		
		assertThrows(CourseNotFoundException.class, () -> cascadeDeleteService
				.deleteCourseAndAllRelatedEntities(1L));
	}
	
	@Test
	@DisplayName("deleteQuestionAndAllRelatedEntities remove question and all related entities")
	void deleteQuestionAndAllRelatedEntities_RemoveQuestionAndAllRelatedEntities_WhenSuccessful() {
		assertDoesNotThrow(() -> cascadeDeleteService.deleteQuestionAndAllRelatedEntities(1L));
	}
	
	@Test
	@DisplayName("deleteQuestionAndAllRelatedEntities Throws QuestionNotFoundException When question not found by id")
	void deleteQuestionAndAllRelatedEntities_ThrowsQuestionNotFoundException_WhenQuestionNotFound() {
		BDDMockito.doThrow(QuestionNotFoundException.class).when(questionServiceMock).delete(anyLong());
		
		assertThrows(QuestionNotFoundException.class, () -> cascadeDeleteService.deleteQuestionAndAllRelatedEntities(1L));
	}
	
	@Test
	@DisplayName("deleteAssignmentAndAllRelatedEntities remove assignment and all related entities when successful")
	void deleteAssignmentAndAllRelatedEntities_RemoveAssignmentAndAllRelatedEntities_WhenSuccessful() {
		assertDoesNotThrow(() -> cascadeDeleteService.deleteAssignmentAndAllRelatedEntities(1L));
	}
	
	@Test
	@DisplayName("deleteAssignmentAndAllRelatedEntities Throws AssignmentNotFoundException When assignment not found by id")
	void deleteAssignmentAndAllRelatedEntities_ThrowsAssignmentNotFoundException_WhenAssignmentNotFound() {
		BDDMockito.doThrow(AssignmentNotFoundException.class).when(assignmentServiceMock).delete(anyLong());
		
		assertThrows(AssignmentNotFoundException.class, () -> cascadeDeleteService
				.deleteAssignmentAndAllRelatedEntities(1L));
	}
	
	@Test
	@DisplayName("deleteQuestionAssignmentAndAllRelatedEntities remove QuestionAssignment and all related entities when successful")
	void deleteQuestionAssignmentAndAllRelatedEntities_RemoveQuestionAssignmentAndAllRelatedEntities_WhenSuccessful() {
		assertDoesNotThrow(() -> cascadeDeleteService.deleteQuestionAssignmentAndAllRelatedEntities(1L));
	}
	
	@Test
	@DisplayName("deleteQuestionAssignmentAndAllRelatedEntities Throws QuestionAssignmentNotFoundException When assignment not found by id")
	void deleteQuestionAssignmentAndAllRelatedEntities_ThrowsQuestionAssignmentNotFoundException_WhenAssignmentNotFound() {
		BDDMockito.doThrow(QuestionAssignmentNotFoundException.class).when(questionAssignmentServiceMock).delete(anyLong());
		
		assertThrows(QuestionAssignmentNotFoundException.class, () -> cascadeDeleteService
				.deleteQuestionAssignmentAndAllRelatedEntities(1L));
	}
}
