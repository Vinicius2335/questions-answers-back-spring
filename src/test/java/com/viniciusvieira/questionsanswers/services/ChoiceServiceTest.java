package com.viniciusvieira.questionsanswers.services;

import static org.junit.jupiter.api.Assertions.*;
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

import com.viniciusvieira.questionsanswers.excepiton.ChoiceNotFoundException;
import com.viniciusvieira.questionsanswers.excepiton.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.repositories.ChoiceRepository;
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
		
		// save
		BDDMockito.when(choiceRepositoryMock.save(any(ChoiceModel.class))).thenReturn(choiceToSave);
		
		// deleteById
		BDDMockito.doNothing().when(choiceRepositoryMock).deleteById(anyLong(), anyLong());
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

}
