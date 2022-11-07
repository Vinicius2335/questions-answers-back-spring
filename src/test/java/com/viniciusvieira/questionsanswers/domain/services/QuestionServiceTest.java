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

import com.viniciusvieira.questionsanswers.domain.excepiton.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.domain.excepiton.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.QuestionRepository;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.CourseCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import com.viniciusvieira.questionsanswers.util.QuestionCreator;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for question service")
class QuestionServiceTest {
	@InjectMocks
	private QuestionService questionService;
	
	@Mock
	private QuestionRepository questionRepositoryMock;
	@Mock
	private ApplicationUserRepository applicationUserRepositoryMock;
	@Mock
	private CourseService courseServiceMock;
	
	private QuestionModel questionToSave;
	private List<QuestionModel> expectedQuestionList;
	private ProfessorModel expectedProfessor;

	@BeforeEach
	void setUp() throws Exception {
		questionToSave = QuestionCreator.mockQuestion();
		expectedQuestionList = List.of(questionToSave);
		expectedProfessor = ProfessorCreator.mockProfessor();
		
		// findOneQuestion
		BDDMockito.when(questionRepositoryMock.findOneQuestion(anyLong(), anyLong()))
				.thenReturn(Optional.of(questionToSave));
		
		// extractProfessorFromToken
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when((String)authentication.getPrincipal())
				.thenReturn(ApplicationUserCreator.mockUserProfessor().getUsername());
		
		BDDMockito.when(applicationUserRepositoryMock.findByUsername(anyString()))
				.thenReturn(ApplicationUserCreator.mockUserProfessor());
		
		// listQuestionByCourseAndTitle
		BDDMockito.when(questionRepositoryMock.listQuestionByCourseAndTitle(anyLong(), anyString(), anyLong()))
				.thenReturn(expectedQuestionList);
		
		// Course findByIdOrThrowCourseNotFoundException
		BDDMockito.when(courseServiceMock.findByIdOrThrowCourseNotFoundException(anyLong()))
				.thenReturn(CourseCreator.mockCourse());
		
		// save
		BDDMockito.when(questionRepositoryMock.save(any(QuestionModel.class)))
				.thenReturn(questionToSave);
		
		// deleteById
		BDDMockito.doNothing().when(questionRepositoryMock).deleteById(anyLong(), anyLong());
	}

	@Test
	@DisplayName("findByIdOrThrowQuestionNotFoundException return question when successful")
	void findByIdOrThrowQuestionNotFoundException_ReturnQuestion_WhenSuccessful() {
		QuestionModel questionFound = questionService.findByIdOrThrowQuestionNotFoundException(1L);
		
		assertAll(
				() -> assertNotNull(questionFound),
				() -> assertEquals(questionToSave, questionFound)
		);
	}
	
	@Test
	@DisplayName("findByIdOrThrowQuestionNotFoundException throw QuestionNotFoundException When question not found")
	void findByIdOrThrowQuestionNotFoundException_ThrowQuestionNotFoundException_WhenQuestionNotFound() {
		BDDMockito.when(questionRepositoryMock.findOneQuestion(anyLong(), anyLong()))
				.thenReturn(Optional.empty());
		
		assertThrows(QuestionNotFoundException.class, () -> questionService
				.findByIdOrThrowQuestionNotFoundException(1L));
	}
	
	@Test
	@DisplayName("findByCourseAndTitle Return a question list when successful")
	void findByCourseAndTitle_ReturnQuestionList_WhenSuccessful() {
		List<QuestionModel> questionListFound = questionService.findByCourseAndTitle(questionToSave.getIdQuestion(), 
				questionToSave.getTitle(), expectedProfessor.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(questionListFound),
				() -> assertFalse(questionListFound.isEmpty()),
				() -> assertTrue(questionListFound.contains(questionToSave))
		);
	}
	
	@Test
	@DisplayName("findByCourseAndTitle Return a empty question list when question not found")
	void findByCourseAndTitle_ReturnEmptyQuestionList_WhenQuestionNotFound() {
		BDDMockito.when(questionRepositoryMock.listQuestionByCourseAndTitle(anyLong(), anyString(), anyLong()))
				.thenReturn(List.of());
		
		List<QuestionModel> questionListFound = questionService.findByCourseAndTitle(questionToSave.getIdQuestion(), 
				questionToSave.getTitle(), expectedProfessor.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(questionListFound),
				() -> assertTrue(questionListFound.isEmpty())
		);
	}
	
	@Test
	@DisplayName("save Insert a question when successful")
	void save_InsertQuestion_WhenSuccessful() {
		QuestionModel questionSaved = questionService.save(QuestionCreator.mockQuestionDto());
		
		assertAll(
				() -> assertNotNull(questionSaved),
				() -> assertEquals(questionToSave, questionSaved)
		);
	}
	
	@Test
	@DisplayName("save Throw CourseNotFoundException when course not found")
	void save_ThrowCourseNotFoundException_WhenCourseNotFound() {
		BDDMockito.when(courseServiceMock.findByIdOrThrowCourseNotFoundException(anyLong()))
				.thenThrow(CourseNotFoundException.class);
	
		assertThrows(CourseNotFoundException.class, () -> questionService
				.save(QuestionCreator.mockQuestionDto()));
	}
	
	@Test
	@DisplayName("replace update a question when successful")
	void save_UpdateQuestion_WhenSuccessful() {
		
		assertDoesNotThrow(() -> questionService.replace(1L, QuestionCreator.mockQuestionDto()));
	}
	
	@Test
	@DisplayName("replace Throw QuestionNotFoundException when question not found")
	void save_ThrowQuestionNotFoundException_WhenQuestionNotFound() {
		BDDMockito.when(questionRepositoryMock.findOneQuestion(anyLong(), anyLong()))
		.thenReturn(Optional.empty());

		assertThrows(QuestionNotFoundException.class, () -> questionService
				.replace(1L, QuestionCreator.mockQuestionDto()));
	}
	
	@Test
	@DisplayName("delete update question enabled field to false when successful")
	void delete_UpdateQuestionEnabledFieldToFalse_WhenSuccessful() {
		assertDoesNotThrow(() -> questionService.delete(1L));
	}
	
	@Test
	@DisplayName("delete throw QuestionNotFoundException When question not found")
	void delete_ThrowQuestionNotFoundException_WhenQuestionNotFound() {
		BDDMockito.when(questionRepositoryMock.findOneQuestion(anyLong(), anyLong()))
				.thenReturn(Optional.empty());
		
		assertThrows(QuestionNotFoundException.class, () -> questionService.delete(1L));
	}
}
