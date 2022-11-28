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
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.viniciusvieira.questionsanswers.domain.exception.ChoiceNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.ConflictException;
import com.viniciusvieira.questionsanswers.domain.exception.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.ChoiceRepository;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.ChoiceCreator;
import com.viniciusvieira.questionsanswers.util.QuestionCreator;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for choice service")
class ChoiceServiceTest {
	
	@InjectMocks
	private ChoiceService choiceService;
	
	@Mock
	private ChoiceRepository choiceRepositoryMock;
	@Mock
	private ApplicationUserRepository applicationUserRepositoryMock;
	@Mock
	private QuestionService questionServiceMock;
	@Mock
	private CourseService courseServiceMock;
	@Mock
	private QuestionAssignmentService questionAssignmentServiceMock;
	
	private ChoiceModel choiceToSave;
	private List<ChoiceModel> expectedChoiceList;

	@BeforeEach
	void setUp() throws Exception {
		choiceToSave = ChoiceCreator.mockChoice();
		expectedChoiceList = List.of(choiceToSave);
		
		// extractProfessorFromToken
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when((String)authentication.getPrincipal())
				.thenReturn(ApplicationUserCreator.mockUserProfessor().getUsername());
		
		BDDMockito.when(applicationUserRepositoryMock.findByUsername(anyString()))
				.thenReturn(ApplicationUserCreator.mockUserProfessor());
		
		// questionService findByIdOrThrowQuestionNotFoundException
		BDDMockito.when(questionServiceMock.findByIdOrThrowQuestionNotFoundException(anyLong()))
				.thenReturn(QuestionCreator.mockQuestion());
		
		// listChoiceByQuestionId
		BDDMockito.when(choiceRepositoryMock.listChoiceByQuestionId(anyLong(), anyLong()))
				.thenReturn(expectedChoiceList);
		
		// findOneChoice
		BDDMockito.when(choiceRepositoryMock.findOneChoice(anyLong(), anyLong()))
				.thenReturn(Optional.of(choiceToSave));
		
		// listChoiceByQuestionAndTitle
		BDDMockito.when(choiceRepositoryMock.listChoiceByQuestionAndTitle(anyLong(), anyString(), anyLong()))
				.thenReturn(expectedChoiceList);
		
		// save
		BDDMockito.when(choiceRepositoryMock.save(any(ChoiceModel.class))).thenReturn(choiceToSave);
		
		// deleteById
		BDDMockito.doNothing().when(choiceRepositoryMock).deleteById(anyLong(), anyLong());
		
		// deleteAllChoicesRelatedToQuestion
		BDDMockito.doNothing().when(choiceRepositoryMock).deleteAllChoicesRelatedToQuestion(anyLong(), anyLong());
		
		// deleteAllChoicesRelatedToCourse
		BDDMockito.doNothing().when(choiceRepositoryMock).deleteAllChoicesRelatedToCourse(anyLong(), anyLong());
		
		// throwsConflictExceptionIfQuestionIsBeingUseInAnyAssignment
		BDDMockito.doNothing().when(questionAssignmentServiceMock)
				.throwsConflictExceptionIfQuestionIsBeingUseInAnyAssignment(anyLong());
		
	}

	@Test
	@DisplayName("listChoiceByQuestion return a list of choices when successful")
	void listChoiceByQuestion_ReturnListChoice_WhenSuccessful() {
		List<ChoiceModel> choiceList = choiceService.listChoiceByQuestion(1L);
		
		assertAll(
				() -> assertNotNull(choiceList),
				() -> assertFalse(choiceList.isEmpty()),
				() -> assertTrue(choiceList.contains(choiceToSave))
		);
	}
	
	@Test
	@DisplayName("listChoiceByQuestion throws QuestionNotFoundException when question not found")
	void listChoiceByQuestion_ThrowsQuestionNotFoundException_WhenQuestionNotFound() {
		BDDMockito.when(questionServiceMock.findByIdOrThrowQuestionNotFoundException(anyLong()))
				.thenThrow(QuestionNotFoundException.class);
		
		assertThrows(QuestionNotFoundException.class, () -> choiceService.listChoiceByQuestion(1L));
	}
	
	@Test
	@DisplayName("listChoiceByQuestion return a empty list of choices when there are no choices for the question")
	void listChoiceByQuestion_ReturnEmptyListChoice_WhenThereAreNoChoicesForTheQuestion() {
		BDDMockito.when(choiceRepositoryMock.listChoiceByQuestionId(anyLong(), anyLong())).thenReturn(List.of());
		
		List<ChoiceModel> choiceList = choiceService.listChoiceByQuestion(1L);
		
		assertAll(
				() -> assertNotNull(choiceList),
				() -> assertTrue(choiceList.isEmpty())
		);
	}
	
	@Test
	@DisplayName("getChoiceByIdOrThrowChoiceNotFoundException return a choice when successful")
	void getChoiceByIdOrThrowChoiceNotFoundException_ReturnChoice_WhenSuccessful() {
		ChoiceModel choiceFound = choiceService.getChoiceByIdOrThrowChoiceNotFoundException(1L);
		
		assertAll(
				() -> assertNotNull(choiceFound),
				() -> assertEquals(choiceToSave, choiceFound)
		);
	}
	
	@Test
	@DisplayName("getChoiceByIdOrThrowChoiceNotFoundException throw ChoiceNotFoundException when choice not found")
	void getChoiceByIdOrThrowChoiceNotFoundException_ThrowChoiceNotFoundException_WhenChoiceNotFound() {
		BDDMockito.when(choiceRepositoryMock.findOneChoice(anyLong(), anyLong()))
				.thenThrow(ChoiceNotFoundException.class);
		
		assertThrows(ChoiceNotFoundException.class, () -> choiceService
				.getChoiceByIdOrThrowChoiceNotFoundException(1L));
	}
	
	@Test
	@DisplayName("findByQuestionAndTitle return a list choice when successful")
	void findByQuestionAndTitle_ReturnListChoice_WhenSuccessful() {
		List<ChoiceModel> findByQuestionAndTitle = choiceService.findByQuestionAndTitle(1L, "Olá?");
		
		assertAll(
				() -> assertNotNull(findByQuestionAndTitle),
				() -> assertFalse(findByQuestionAndTitle.isEmpty()),
				() -> assertEquals(1, findByQuestionAndTitle.size()),
				() -> assertTrue(findByQuestionAndTitle.contains(choiceToSave))
		);
	}
	
	@Test
	@DisplayName("findByQuestionAndTitle return a empty list choice when choice not found")
	void findByQuestionAndTitle_ReturnEmptyListChoice_WhenChoiceNotFound() {
		BDDMockito.when(choiceRepositoryMock.listChoiceByQuestionAndTitle(anyLong(), anyString(), anyLong()))
				.thenReturn(List.of());
		
		List<ChoiceModel> findByQuestionAndTitle = choiceService.findByQuestionAndTitle(1L, "Olá?");
		
		assertAll(
				() -> assertNotNull(findByQuestionAndTitle),
				() -> assertTrue(findByQuestionAndTitle.isEmpty())
		);
	}
	
	@Test
	@DisplayName("findByQuestionAndTitle Throws QuestionNotFoundException when question not found")
	void findByQuestionAndTitle_ThrowsQuestionNotFoundException_WhenQuestionNotFound() {
		BDDMockito.when(questionServiceMock.findByIdOrThrowQuestionNotFoundException(anyLong()))
				.thenThrow(QuestionNotFoundException.class);
		
		assertThrows(QuestionNotFoundException.class, () -> choiceService.findByQuestionAndTitle(1L, "Olá?"));
	}
		
	@Test
	@DisplayName("save return choice when successful")
	void save_ReturnChoice_WhenSuccessful() {
		ChoiceModel choiceSaved = choiceService.save(ChoiceCreator.mockChoiceDto());
		
		assertAll(
				() -> assertNotNull(choiceSaved),
				() -> assertEquals(choiceToSave, choiceSaved)
		);
	}
	
	@Test
	@DisplayName("save throw QuestionNotFoundException when question not found")
	void save_ThrwosQuestionNotFoundException_WhenQuestionNotFound() {
		BDDMockito.when(questionServiceMock.findByIdOrThrowQuestionNotFoundException(anyLong()))
				.thenThrow(QuestionNotFoundException.class);
		
		assertThrows(QuestionNotFoundException.class, () -> choiceService.save(ChoiceCreator.mockChoiceDto()));
	}
	
	@Test
	@DisplayName("replace updated choice when successful")
	void replace_UpdateChoice_WhenSuccessful() {
		assertDoesNotThrow(() -> choiceService.replace(1L, ChoiceCreator.mockChoiceDto()));
	}
	
	@Test
	@DisplayName("replace throw QuestionNotFoundException when question not found")
	void replace_ThrowQuestionNotFoundException_WhenQuestionNotFound() {
		BDDMockito.when(questionServiceMock.findByIdOrThrowQuestionNotFoundException(anyLong()))
				.thenThrow(QuestionNotFoundException.class);
		
		assertThrows(QuestionNotFoundException.class, () -> choiceService.replace(1L, ChoiceCreator.mockChoiceDto()));
	}
	
	@Test
	@DisplayName("replace throw ChoiceNotFoundException when choice not found")
	void replace_ThrowChoiceNotFoundException_WhenChoiceNotFound() {
		BDDMockito.when(choiceRepositoryMock.findOneChoice(anyLong(), anyLong()))
				.thenThrow(ChoiceNotFoundException.class);
		
		assertThrows(ChoiceNotFoundException.class, () -> choiceService.replace(1L, ChoiceCreator.mockChoiceDto()));
	}
	
	@Test
	@DisplayName("delete remove choice when successful")
	void delete_RemoveChoice_WhenSuccessful() {
		assertDoesNotThrow(() -> choiceService.delete(1L));
	}
	
	@Test
	@DisplayName("delete throw ChoiceNotFoundException when choice not found")
	void delete_ThrowChoiceNotFoundException_WhenChoiceNotFound() {
		BDDMockito.when(choiceRepositoryMock.findOneChoice(anyLong(), anyLong()))
				.thenThrow(ChoiceNotFoundException.class);
		
		assertThrows(ChoiceNotFoundException.class, () -> choiceService.delete(1L));
	}
	
	@Test
	@DisplayName("delete throw QuestionNotFoundException when question not found")
	void delete_ThrowQuestionNotFoundException_WhenQuestionNotFound() {
		BDDMockito.when(questionServiceMock.findByIdOrThrowQuestionNotFoundException(anyLong()))
		.thenThrow(QuestionNotFoundException.class);
		
		assertThrows(QuestionNotFoundException.class, () -> choiceService.delete(1L));
	}
	
	@Test
	@DisplayName("delete throw ConflictException when choice is associated with a questionAssignment")
	void delete_ThrowConflictException_WhenChoiceIsAssociatedWithQuestionAssignment() {
		BDDMockito.doThrow(ConflictException.class).when(questionAssignmentServiceMock)
				.throwsConflictExceptionIfQuestionIsBeingUseInAnyAssignment(anyLong());
		
		assertThrows(ConflictException.class, () -> choiceService.delete(1L));
	}
	
	@Test
	@DisplayName("deleteAllChoicesRelatedToQuestion Remove choice related to question removed when successful")
	void deleteAllChoicesRelatedToQuestion_RemoveChoiceRelatedQuestionRemoved_WhenSuccessful() {
		assertDoesNotThrow(() -> choiceService.deleteAllChoicesRelatedToQuestion(1L));
	}
	
	@Test
	@DisplayName("deleteAllChoicesRelatedToQuestion throw QuestionNotFoundException when question not found")
	void deleteAllChoicesRelatedToQuestion_ThrowQuestionNotFoundException_WhenQuestionNotFound() {
		BDDMockito.when(questionServiceMock.findByIdOrThrowQuestionNotFoundException(anyLong()))
				.thenThrow(QuestionNotFoundException.class);
		
		assertThrows(QuestionNotFoundException.class, () -> choiceService.deleteAllChoicesRelatedToQuestion(1L));
	}
	
	@Test
	@DisplayName("deleteAllChoicesRelatedToCourse Remove choice related to course removed when successful")
	void deleteAllChoicesRelatedToCourse_RemoveChoiceRelatedCourseRemoved_WhenSuccessful() {
		assertDoesNotThrow(() -> choiceService.deleteAllChoicesRelatedToCourse(1L));
	}
	
	@Test
	@DisplayName("deleteAllChoicesRelatedToCourse throw CourseNotFoundException when course not found")
	void deleteAllChoicesRelatedToCourse_ThrowCourseNotFoundException_WhenCourseNotFound() {
		BDDMockito.when(courseServiceMock.findByIdOrThrowCourseNotFoundException(anyLong()))
				.thenThrow(CourseNotFoundException.class);
		
		assertThrows(CourseNotFoundException.class, () -> choiceService.deleteAllChoicesRelatedToCourse(1L));
	}

}
