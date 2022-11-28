package com.viniciusvieira.questionsanswers.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;

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

import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.CourseRepository;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for extract professor service")
class ExtractEntityFromTokenServiceTest {
	@InjectMocks
	private ExtractEntityFromTokenService extractEntityFromTokenService;
	
	@Mock
	private CourseRepository courseRepositoryMock;
	@Mock
	private ApplicationUserRepository applicationUserRepositoryMock;
	
	private ProfessorModel expectedProfessor;

	@BeforeEach
	void setUp() throws Exception {
		expectedProfessor = ProfessorCreator.mockProfessor();
		
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		
		SecurityContextHolder.setContext(securityContext);
		
		Mockito.when((String)authentication.getPrincipal())
				.thenReturn(ApplicationUserCreator.mockUserProfessor().getUsername());
		
		BDDMockito.when(applicationUserRepositoryMock.findByUsername(anyString()))
				.thenReturn(ApplicationUserCreator.mockUserProfessor());
	}

	@Test
	@DisplayName("extractProfessorFromToken returna a professor when successful")
	void extractProfessorFromToken_ReturnProfessor_WhenSuccessful() {
		ProfessorModel professor = extractEntityFromTokenService.extractProfessorFromToken();
		
		assertNotNull(professor);
		assertEquals(expectedProfessor, professor);
	}

}
