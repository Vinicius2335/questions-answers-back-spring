package com.viniciusvieira.questionsanswers.domain.repositories;

import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.StudentModel;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import com.viniciusvieira.questionsanswers.util.RoleCreator;
import com.viniciusvieira.questionsanswers.util.StudentCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Test for application user repository")
class ApplicationUserRepositoryTest {
	@Autowired
	private ApplicationUserRepository applicationUserRepository;
	
	@Autowired
	private ProfessorRepository professorRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	private ApplicationUserModel userProfessorToSave;
	private ApplicationUserModel userStudentToSave;

	@BeforeEach
	void setUp() {
		professorRepository.save(ProfessorCreator.mockProfessor());
		roleRepository.save(RoleCreator.mockRoleProfessor());
		roleRepository.save(RoleCreator.mockRoleStudent());
		
		userProfessorToSave = ApplicationUserCreator.mockUserProfessor();
		userStudentToSave = ApplicationUserCreator.mockUserStudent();
	}

	@Test
	@DisplayName("findByUsername return a application user when succesful")
	void findByUsername_ReturnApplicationUser_WhenSuccessful() {
		ApplicationUserModel userProfessorSaved = applicationUserRepository.save(userProfessorToSave);
		
		ApplicationUserModel userFound = applicationUserRepository.findByUsername(userProfessorSaved.getUsername());
		
		assertAll(
				() -> assertNotNull(userFound),
				() -> assertEquals(userProfessorToSave, userFound)
		);
	}

	@Test
	@DisplayName("findByUsername return a null application user when user not found by username")
	void findByUsername_ReturnNullApplicationUser_WhenUserNotFoundByUsername() {
		ApplicationUserModel userFound = applicationUserRepository.findByUsername("vinicius");

		assertNull(userFound);
	}

	@Test
	@DisplayName("findByStudent return a application user when successful")
	void findByStudent_ReturnApplicationUser_WhenSuccessful(){
		ApplicationUserModel userStudentSaved = applicationUserRepository.save(userStudentToSave);

		ApplicationUserModel userFound = applicationUserRepository.findByStudent(userStudentSaved.getStudent());

		assertAll(
				() -> assertNotNull(userFound),
				() -> assertEquals(userStudentSaved, userFound)
		);
	}

	@Test
	@DisplayName("findByStudent return a null application user when user not found by student")
	void findByStudent_ReturnNullApplicationUser_WhenUserNotFoundByStudent(){
		StudentModel student = StudentCreator.mockStudent();
		ApplicationUserModel userFound = applicationUserRepository.findByStudent(student);

		assertNull(userFound);
	}

	@Test
	@DisplayName("findByProfessor return a application user when successful")
	void findByProfessor_ReturnApplicationUser_WhenSuccessful(){
		ApplicationUserModel userProfessorSaved = applicationUserRepository.save(userProfessorToSave);
		ApplicationUserModel userFound = applicationUserRepository.findByProfessor(userProfessorSaved.getProfessor());

		assertAll(
				() -> assertNotNull(userFound),
				() -> assertEquals(userProfessorSaved, userFound)
		);
	}

	@Test
	@DisplayName("findByProfessor return a null application user when user not found by professor")
	void findByProfessor_ReturnNullApplicationUser_WhenUserNotFoundByProfessor(){
		ProfessorModel professor = ProfessorCreator.mockProfessor();
		ApplicationUserModel userFound = applicationUserRepository.findByProfessor(professor);

		assertNull(userFound);
	}


}
