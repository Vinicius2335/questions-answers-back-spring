package com.viniciusvieira.questionsanswers.domain.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.viniciusvieira.questionsanswers.api.representation.models.QuestionAssignmentDto;
import com.viniciusvieira.questionsanswers.domain.excepiton.AssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.excepiton.ConflictException;
import com.viniciusvieira.questionsanswers.domain.excepiton.QuestionAssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.excepiton.QuestionAssignmetAlreadyExistsException;
import com.viniciusvieira.questionsanswers.domain.excepiton.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionAssignmentModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.repositories.QuestionAssignmentRepository;
import com.viniciusvieira.questionsanswers.util.AssignmentCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import com.viniciusvieira.questionsanswers.util.QuestionAssignmentCreator;
import com.viniciusvieira.questionsanswers.util.QuestionCreator;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for questionAssignment service")
class QuestionAssignmentServiceTest {
	@InjectMocks
	private QuestionAssignmentService questionAssignmentService;
	
	@Mock
	private QuestionAssignmentRepository questionAssignmentRepositoryMock;
	@Mock
	private QuestionService questionServiceMock;
	@Mock
	private AssignmentService assignmentServiceMock;
	@Mock
	private ExtractProfessorService extractProfessorServiceMock;
	
	private QuestionAssignmentModel expectedQuestionAssignment;
	private List<QuestionAssignmentModel> expectedQuestionAssignmentList;
	private ProfessorModel professorMock;
	private QuestionModel questionMock;
	private AssignmentModel assignmentMock;

	@BeforeEach
	void setUp() throws Exception {
		expectedQuestionAssignment = QuestionAssignmentCreator.mockQuestionAssignment();
		expectedQuestionAssignmentList = List.of(expectedQuestionAssignment);
		professorMock = ProfessorCreator.mockProfessor();
		questionMock = QuestionCreator.mockQuestion2Choices();
		assignmentMock = AssignmentCreator.mockAssignment();
		
		// extractProfessorService extractProfessorFromToken
		BDDMockito.when(extractProfessorServiceMock.extractProfessorFromToken())
				.thenReturn(professorMock);
		
		// findOneQuestionAssignment
		BDDMockito.when(questionAssignmentRepositoryMock.findOneQuestionAssignment(anyLong(), anyLong()))
				.thenReturn(Optional.of(expectedQuestionAssignment));
		
		// questionService listQuestionsByCourseNotAssociatedWithAnAssignment
		BDDMockito.when(questionServiceMock
				.listQuestionsByCourseNotAssociatedWithAnAssignment(anyLong(), anyLong()))
				.thenReturn(List.of(questionMock));
		
		// questionService findByIdOrThrowQuestionNotFoundException
		BDDMockito.when(questionServiceMock.findByIdOrThrowQuestionNotFoundException(anyLong()))
				.thenReturn(questionMock);
		
		// assignmentService findAssignmentOrThrowsAssignmentNotFoundException
		BDDMockito.when(assignmentServiceMock.findAssignmentOrThrowsAssignmentNotFoundException(anyLong()))
				.thenReturn(assignmentMock);
		
		// listQuestionAssignmentByAssignmentId
		BDDMockito.when(questionAssignmentRepositoryMock.listQuestionAssignmentByAssignmentId(anyLong(), anyLong()))
				.thenReturn(expectedQuestionAssignmentList);
		
		// save
		BDDMockito.when(questionAssignmentRepositoryMock.save(any(QuestionAssignmentModel.class)))
				.thenReturn(expectedQuestionAssignment);
		
		// listQuestionAssignmentByQuestionAndAssignment
		BDDMockito.when(questionAssignmentRepositoryMock
				.listQuestionAssignmentByQuestionAndAssignment(anyLong(), anyLong(), anyLong()))
				.thenReturn(List.of());
		
		// delete
		BDDMockito.doNothing().when(questionAssignmentRepositoryMock).deleteById(anyLong(), anyLong());
		
		// listQuestionAssignmentByQuestionId
		BDDMockito.when(questionAssignmentRepositoryMock.listQuestionAssignmentByQuestionId(anyLong(), anyLong()))
				.thenReturn(List.of());
		
		
	}

	@Test
	@DisplayName("findQuestionAssignmentOrThrowsQuestionAssignmentNotFound return a questionAssignment when successful")
	void findQuestionAssignmentOrThrowsQuestionAssignmentNotFound_ReturnQuestionAssignment_WhenSuccessful() {
		QuestionAssignmentModel questionAssignmentFound = questionAssignmentService
				.findQuestionAssignmentOrThrowsQuestionAssignmentNotFound(1L);
		
		assertAll(
				() -> assertNotNull(questionAssignmentFound),
				() -> assertEquals(expectedQuestionAssignment.getIdQuestionAssignment(),
						questionAssignmentFound.getIdQuestionAssignment()),
				() -> assertEquals(expectedQuestionAssignment.getAssignment(),
						questionAssignmentFound.getAssignment()),
				() -> assertEquals(expectedQuestionAssignment.getQuestion(),
						questionAssignmentFound.getQuestion())
		);
	}
	
	@Test
	@DisplayName("findQuestionAssignmentOrThrowsQuestionAssignmentNotFound Throws QuestionAssignmenNotFoundException when questionAssignmen not found")
	void findQuestionAssignmentOrThrowsQuestionAssignmentNotFound_ThrowsQuestionAssignmentNotFoundException_WhenQuestionAssignmentNotFound() {
		BDDMockito.when(questionAssignmentRepositoryMock.findOneQuestionAssignment(anyLong(), anyLong()))
				.thenReturn(Optional.empty());
		
		assertThrows(QuestionAssignmentNotFoundException.class, () -> questionAssignmentService
				.findQuestionAssignmentOrThrowsQuestionAssignmentNotFound(1L));
	}
	
	@Test
	@DisplayName("findQuestionById return a questionList when successful")
	void findQuestionById_ReturnQuestionList_WhenSuccessful() {
		List<QuestionModel> questions = questionAssignmentService
				.findQuestionById(1L, professorMock.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(questions),
				() -> assertFalse(questions.isEmpty()),
				() -> assertTrue(questions.contains(questionMock))
		);
	}
	
	@Test
	@DisplayName("findQuestionById Throws QuestionNotFoundException when question not found")
	void findQuestionById_ThrowsQuestionNotFoundException_WhenQuestionNotFound() {
		BDDMockito.when(questionServiceMock
				.listQuestionsByCourseNotAssociatedWithAnAssignment(anyLong(), anyLong()))
				.thenReturn(List.of());
		
		assertThrows(QuestionNotFoundException.class, () -> questionAssignmentService
				.findQuestionById(1L, professorMock.getIdProfessor()));
	}
	
	@Test
	@DisplayName("listByAssignment return a questionAssignmentList when successful")
	void listByAssignment_ReturnQuestionAssignmentList_WhenSuccessful() {
		List<QuestionAssignmentModel> questionAssignments = questionAssignmentService.listByAssignment(1L);
		
		assertAll(
				() -> assertNotNull(questionAssignments),
				() -> assertFalse(questionAssignments.isEmpty()),
				() -> assertTrue(questionAssignments.contains(expectedQuestionAssignment))
		);
	}
	
	@Test
	@DisplayName("listByAssignment Throws AssignmentNotFoundException when assignment not found")
	void listByAssignment_ThrowsAssignmentNotFoundException_WhenAssignmentNotFound() {
		BDDMockito.when(assignmentServiceMock.findAssignmentOrThrowsAssignmentNotFoundException(anyLong()))
				.thenThrow(AssignmentNotFoundException.class);
		
		assertThrows(AssignmentNotFoundException.class, () -> questionAssignmentService.listByAssignment(1L));
	}
	
	@Test
	@DisplayName("save insert and return a question assignment saved when successful")
	void save_InsertAndReturnQuestionAssignmentSaved_WhenSuccessful() {
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDto();
		QuestionAssignmentModel questionAssignmentSaved = questionAssignmentService.save(questionAssignmentDto);
		
		assertAll(
				()-> assertNotNull(questionAssignmentSaved),
				() -> assertEquals(expectedQuestionAssignment, questionAssignmentSaved)
		);
	}
	
	@Test
	@DisplayName("save Throws AssignmentNotFoundException when assignment not found")
	void save_ThrowsAssignmentNotFoundException_WhenAssignmentNotFound() {
		BDDMockito.when(assignmentServiceMock.findAssignmentOrThrowsAssignmentNotFoundException(anyLong()))
				.thenThrow(AssignmentNotFoundException.class);
		
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDto();
		
		assertThrows(AssignmentNotFoundException.class, () -> questionAssignmentService
				.save(questionAssignmentDto));
	}
	
	@Test
	@DisplayName("save Throws QuestionNotFoundException when question not found")
	void save_ThrowsQuestionNotFoundException_WhenQuestionNotFound() {
		BDDMockito.when(questionServiceMock.findByIdOrThrowQuestionNotFoundException(anyLong()))
				.thenThrow(QuestionNotFoundException.class);
		
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDto();
		
		assertThrows(QuestionNotFoundException.class, () -> questionAssignmentService
				.save(questionAssignmentDto));
	}
	
	@Test
	@DisplayName("save Throws QuestionAssignmetAlreadyExistsException when Question Assignment Association Already Exists")
	void save_ThrowsQuestionAssignmetAlreadyExistsException_WhenQuestionNotFound() {
		BDDMockito.when(questionAssignmentRepositoryMock
				.listQuestionAssignmentByQuestionAndAssignment(anyLong(), anyLong(), anyLong()))
				.thenReturn(expectedQuestionAssignmentList);
		
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDto();
		
		assertThrows(QuestionAssignmetAlreadyExistsException.class, () -> questionAssignmentService
				.save(questionAssignmentDto));
	}
	
	@Test
	@DisplayName("replace update a questionAssignment when successful")
	void replace_UpdateQuestionAssignment_WhenSuccessful() {
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDto();
		
		assertDoesNotThrow(() -> questionAssignmentService.replace(1L, questionAssignmentDto));
	}
	
	@Test
	@DisplayName("replace Throws QuestionAssignmenNotFoundException when questionAssignment not found")
	void replace_ThrowsQuestionAssignmentNotFoundException_WhenQuestionAssignmentNotFound() {
		BDDMockito.when(questionAssignmentRepositoryMock.findOneQuestionAssignment(anyLong(), anyLong()))
				.thenReturn(Optional.empty());
		
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDto();
		
		assertThrows(QuestionAssignmentNotFoundException.class,
				() -> questionAssignmentService.replace(1L, questionAssignmentDto));
	}
	
	@Test
	@DisplayName("delete Remove a questionAssignment when successful")
	void delete_RemoveQuestionAssignment_WhenSusccessful() {
		assertDoesNotThrow(() -> questionAssignmentService.delete(1L));
	}
	
	@Test
	@DisplayName("delete Throws QuestionAssignmenNotFoundException when questionAssignment not found")
	void delete_ThrowsQuestionAssignmentNotFoundException_WhenQuestionAssignmentNotFound() {
		BDDMockito.when(questionAssignmentRepositoryMock.findOneQuestionAssignment(anyLong(), anyLong()))
				.thenReturn(Optional.empty());
		
		assertThrows(QuestionAssignmentNotFoundException.class,
				() -> questionAssignmentService.delete(1L));
	}
	
	@Test
	@DisplayName("deleteAllQuestionAssignmentRelatedToCourse remove all questionAssignment related to course when successful")
	void deleteAllQuestionAssignmentRelatedToCourse_RemoveAllQuestionAssignmentRelatedToCourse_WhenSuccessful() {
		BDDMockito.doNothing().when(questionAssignmentRepositoryMock)
				.deleteAllQuestionAssignmentRelatedToCourse(anyLong(), anyLong());
		
		assertDoesNotThrow(() -> questionAssignmentService.deleteAllQuestionAssignmentRelatedToCourse(1L));
	}
	
	@Test
	@DisplayName("deleteAllQuestionAssignmentRelatedToAssignment remove all questionAssignment related to assignment when successful")
	void deleteAllQuestionAssignmentRelatedToAssignment_RemoveAllQuestionAssignmentRelatedToAssignment_WhenSuccessful() {
		BDDMockito.doNothing().when(questionAssignmentRepositoryMock)
		.deleteAllQuestionAssignmentRelatedToAssignment(anyLong(), anyLong());
		
		assertDoesNotThrow(() -> questionAssignmentService.deleteAllQuestionAssignmentRelatedToAssignment(1L));
	}
	
	@Test
	@DisplayName("deleteAllQuestionAssignmentRelatedToQuestion remove all questionAssignment related to question when successful")
	void deleteAllQuestionAssignmentRelatedToQuestion_RemoveAllQuestionAssignmentRelatedToQuestion_WhenSuccessful() {
		BDDMockito.doNothing().when(questionAssignmentRepositoryMock)
		.deleteAllQuestionAssignmentRelatedToAssignment(anyLong(), anyLong());
		
		assertDoesNotThrow(() -> questionAssignmentService.deleteAllQuestionAssignmentRelatedToQuestion(1L));
	}
	
	@Test
	@DisplayName("throwsConflictExceptionIfQuestionIsBeingUseInAnyAssignment return void when Don't Exists Any Conflict")
	void throwsConflictExceptionIfQuestionIsBeingUseInAnyAssignment_ReturnVoid_WhenDontExistsAnyConflict() {
		assertDoesNotThrow(() -> questionAssignmentService
				.throwsConflictExceptionIfQuestionIsBeingUseInAnyAssignment(1L));
	}
	
	@Test
	@DisplayName("throwsConflictExceptionIfQuestionIsBeingUseInAnyAssignment ThrowsConflictException when Choice associated a questionAssignment")
	void throwsConflictExceptionIfQuestionIsBeingUseInAnyAssignment_ThrowsConflictException_WhenChoiceAssociatedQuestionAssignment() {
		BDDMockito.when(questionAssignmentRepositoryMock.listQuestionAssignmentByQuestionId(anyLong(), anyLong()))
			.thenReturn(expectedQuestionAssignmentList);
		
		assertThrows(ConflictException.class, () -> questionAssignmentService
				.throwsConflictExceptionIfQuestionIsBeingUseInAnyAssignment(1L));
	}
}
