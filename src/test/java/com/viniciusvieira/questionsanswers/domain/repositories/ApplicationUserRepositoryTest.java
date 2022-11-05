package com.viniciusvieira.questionsanswers.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.ProfessorRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.RoleRepository;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import com.viniciusvieira.questionsanswers.util.RoleCreator;

@DataJpaTest
@DisplayName("Test for application user repository")
class ApplicationUserRepositoryTest {
	
	@Autowired
	private ApplicationUserRepository applicationUserRepository;
	@Autowired
	private ProfessorRepository professorRepository;
	@Autowired
	private RoleRepository roleRepository;
	private ApplicationUserModel userToSave;
	
	@BeforeEach
	void setUp() throws Exception {
		professorRepository.save(ProfessorCreator.mockProfessor());
		roleRepository.save(RoleCreator.mockRoleProfessor());
		
		userToSave = ApplicationUserCreator.mockUserProfessor();
	}

	@Test
	@DisplayName("findByUsername return a application user when succesful")
	void findByUsername_ReturnApplicationUser_WhenSuccessful() {
		ApplicationUserModel userSaved = applicationUserRepository.save(userToSave);
		
		ApplicationUserModel userFound = applicationUserRepository.findByUsername(userSaved.getUsername());
		
		assertAll(
				() -> assertNotNull(userFound),
				() -> assertEquals(userToSave, userFound)
		);
		
	}

}
