package com.viniciusvieira.questionsanswers.domain.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.AssignmentCreator;
import com.viniciusvieira.questionsanswers.util.CourseCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import com.viniciusvieira.questionsanswers.util.RoleCreator;

@DataJpaTest
@DisplayName("Test for assignment repository")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class AssignmentRepositoryTest {
	@Autowired
	private AssignmentRepository assignmentRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private ProfessorRepository professorRepository;
	@Autowired
	private ApplicationUserRepository applicationUserRepository;
	@Autowired
	private CourseRepository courseRepository;
	
	private Long professorIdSaved;
	private AssignmentModel expectedAssignment;
	private CourseModel courseSaved;

	@BeforeEach
	void setUp() throws Exception {
		roleRepository.save(RoleCreator.mockRoleProfessor());
		professorIdSaved = professorRepository.save(ProfessorCreator.mockProfessor()).getIdProfessor();
		applicationUserRepository.save(ApplicationUserCreator.mockUserProfessor());
		courseSaved = courseRepository.save(CourseCreator.mockCourse());
		
		expectedAssignment = AssignmentCreator.mockAssignment();
	}
	
	public AssignmentModel insertAssignment() {
		return assignmentRepository.save(expectedAssignment);
	}

	@Test
	@DisplayName("save insert assignment when successful")
	void save_InsertAssignment_WhenSuccessful() {
		AssignmentModel assignmentSaved = assignmentRepository.save(expectedAssignment);
		
		assertAll(
				() -> assertNotNull(assignmentSaved),
				() -> assertEquals(expectedAssignment, assignmentSaved)
		);
	}
	
	@Test
	@DisplayName("findOneAssignment return a optional assignment when successful")
	void findOneAssignment_ReturnOptionalAssignmentFoundById_WhenSuccessful() {
		AssignmentModel assignmentSaved = insertAssignment();
		
		Optional<AssignmentModel> assignmentFound = assignmentRepository.findOneAssignment(
				assignmentSaved.getIdAssignment(), professorIdSaved);
		
		assertAll(
				() -> assertNotNull(assignmentFound),
				() -> assertFalse(assignmentFound.isEmpty()),
				() -> assertTrue(assignmentFound.get().equals(expectedAssignment))
		);
	}
	
	@Test
	@DisplayName("findOneAssignment return a empty optional assignment when assignment not found")
	void findOneAssignment_ReturnEmptyOptionalAssignmentFoundById_WhenAssignmentNotFound() {
		Optional<AssignmentModel> assignmentFound = assignmentRepository.findOneAssignment(
				1L, professorIdSaved);
		
		assertAll(
				() -> assertNotNull(assignmentFound),
				() -> assertTrue(assignmentFound.isEmpty())
		);
	}
	
	@Test
	@DisplayName("listAssignmentByCourseAndTitle return a assignment list when successful")
	void listAssignmentByCourseAndTitle_ReturnAssignmentList_WhenSuccessful() {
		AssignmentModel assignmentSaved = insertAssignment();
		List<AssignmentModel> assignmentsFoundList = assignmentRepository.listAssignmentByCourseAndTitle
				(assignmentSaved.getIdAssignment(),assignmentSaved.getTitle(), professorIdSaved);
		
		assertAll(
				() -> assertNotNull(assignmentsFoundList),
				() -> assertFalse(assignmentsFoundList.isEmpty()),
				() -> assertEquals(1, assignmentsFoundList.size()),
				() -> assertTrue(assignmentsFoundList.contains(expectedAssignment))
		);
	}
	
	@Test
	@DisplayName("listAssignmentByCourseAndTitle return a assignment empty list when assignment not found")
	void listAssignmentByCourseAndTitle_ReturnAssignmentEmptyList_WhenAssignmentNotFound() {
		List<AssignmentModel> assignmentsFoundList = assignmentRepository.listAssignmentByCourseAndTitle
				(1L, "teste", professorIdSaved);
		
		assertAll(
				() -> assertNotNull(assignmentsFoundList),
				() -> assertTrue(assignmentsFoundList.isEmpty())
		);
	}
	
	@Test
	@DisplayName("deleteById delete a assignment when successful")
	void deleteById_DeleteAssignment_WhenSuccessful() {
		AssignmentModel assignmentSaved = insertAssignment();
		
		assertDoesNotThrow(() -> assignmentRepository.deleteById(assignmentSaved.getIdAssignment(),
				professorIdSaved));
		
		List<AssignmentModel> testFindAllEnabledFalse = assignmentRepository.testFindAllEnabledFalse();
				
		assertAll(
				() -> assertNotNull(testFindAllEnabledFalse),
				() -> assertFalse(testFindAllEnabledFalse.isEmpty()),
				() -> assertEquals(1, testFindAllEnabledFalse.size())
		);
	}
	
	@Test
	@DisplayName("deleteAllAssignmentRelatedToCourse delete a assignment when successful")
	void deleteAllAssignmentRelatedToCourse_DeleteAssignment_WhenSuccessful() {
		insertAssignment();
		
		assertDoesNotThrow(() -> assignmentRepository.deleteAllAssignmentRelatedToCourse(courseSaved.getIdCourse(),
				professorIdSaved));
		
		List<AssignmentModel> testFindAllEnabledFalse = assignmentRepository.testFindAllEnabledFalse();
		
		assertAll(
				() -> assertNotNull(testFindAllEnabledFalse),
				() -> assertFalse(testFindAllEnabledFalse.isEmpty()),
				() -> assertEquals(1, testFindAllEnabledFalse.size())
		);
	}
	
	@Test
	@DisplayName("accessCodeExistsForCourse return Empty Optional when accessCode don't exists")
	void accessCodeExistsForCourse_ReturnEmptyOptional_WhenAccessCodeDontExists(){
		insertAssignment();
		
		Optional<AssignmentModel> accessCodeExistsForCourse = assignmentRepository
				.accessCodeExistsForCourse("9999", 1L, professorIdSaved);
		
		assertTrue(accessCodeExistsForCourse.isEmpty());
	}
	
	@Test
	@DisplayName("accessCodeExistsForCourse return Optional Assignment when accessCode exists")
	void accessCodeExistsForCourse_ReturnOptionalAssignment_WhenAccessCodeExists(){
		AssignmentModel assignmentSaved = insertAssignment();
		
		Optional<AssignmentModel> accessCodeExistsForCourse = assignmentRepository
				.accessCodeExistsForCourse(assignmentSaved.getAccessCode(),
						assignmentSaved.getCourse().getIdCourse(), professorIdSaved);
		
		assertTrue(accessCodeExistsForCourse.isEmpty());
	}

}
