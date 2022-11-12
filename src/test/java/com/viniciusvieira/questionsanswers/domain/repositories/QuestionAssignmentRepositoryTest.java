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

import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionAssignmentModel;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.AssignmentCreator;
import com.viniciusvieira.questionsanswers.util.ChoiceCreator;
import com.viniciusvieira.questionsanswers.util.CourseCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import com.viniciusvieira.questionsanswers.util.QuestionAssignmentCreator;
import com.viniciusvieira.questionsanswers.util.QuestionCreator;
import com.viniciusvieira.questionsanswers.util.RoleCreator;

@DataJpaTest
@DisplayName("Test for question assignment repository")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class QuestionAssignmentRepositoryTest {
	@Autowired
	private QuestionAssignmentRepository questionAssignmentRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private ProfessorRepository professorRepository;
	@Autowired
	private ApplicationUserRepository applicationUserRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private ChoiceRepository choiceRepository;
	@Autowired
	private AssignmentRepository assignmentRepository;
	@Autowired
	private QuestionRepository questionRepository;
	
	private QuestionAssignmentModel expectedQuestionAssignment;
	private ProfessorModel professorToSave;

	@BeforeEach
	void setUp() throws Exception {
		expectedQuestionAssignment = QuestionAssignmentCreator.mockQuestionAssignment();
		
		insertValues();
	}
	

	@Test
	@DisplayName("save insert a questionAssingment when successful")
	// obs: assignment possui um campo final com data oq dificulta para validar se os objetos sao iguais
	void save_InsertQuestionAssignment_WheSuccessful() {
		QuestionAssignmentModel questionAssignmentSaved = questionAssignmentRepository
				.save(expectedQuestionAssignment);
		
		assertAll(
				() -> assertNotNull(questionAssignmentSaved),
				() -> assertEquals(expectedQuestionAssignment.getQuestion(),
						questionAssignmentSaved.getQuestion()),
				() -> assertEquals(expectedQuestionAssignment.getIdQuestionAssignment(),
						questionAssignmentSaved.getIdQuestionAssignment()),
				() -> assertEquals(expectedQuestionAssignment.getProfessor(),
						questionAssignmentSaved.getProfessor())
		);
	}
	
	@Test
	@DisplayName("findOneQuestionAssignment return a Optional QuestionAssignment when successful")
	void findOneQuestionAssignment_ReturnOptionalQuestionAssignment_WhenSuccessful() {
		QuestionAssignmentModel questionAssignmentSave = insertQuestionAssingment();
		
		Optional<QuestionAssignmentModel> questionAssignmentFound = questionAssignmentRepository
				.findOneQuestionAssignment(questionAssignmentSave.getIdQuestionAssignment(),
						professorToSave.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(questionAssignmentFound),
				() -> assertFalse(questionAssignmentFound.isEmpty()),
				() -> assertEquals(questionAssignmentSave, questionAssignmentFound.get())
		);
	}
	
	@Test
	@DisplayName("findOneQuestionAssignment return a Empty Optional QuestionAssignment when questionAssignment not found")
	void findOneQuestionAssignment_ReturnEmptyOptionalQuestionAssignment_WhenQuestionAssignmentNotFound() {
		Optional<QuestionAssignmentModel> questionAssignmentFound = questionAssignmentRepository
				.findOneQuestionAssignment(1L, 1L);
		
		assertAll(
				() -> assertNotNull(questionAssignmentFound),
				() -> assertTrue(questionAssignmentFound.isEmpty())
		);
	}
	
	@Test
	@DisplayName("listQuestionAssignmentByQuestionAndAssignment return a QuestionAssignmentList when successful")
	void listQuestionAssignmentByQuestionAndAssignment_ReturnQuestionAssignmentList_WhenSuccessful() {
		QuestionAssignmentModel questionAssingmentSaved = insertQuestionAssingment();
		
		List<QuestionAssignmentModel> questionAssignmentList = questionAssignmentRepository
				.listQuestionAssignmentByQuestionAndAssignment(1L, 1L, professorToSave.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(questionAssignmentList),
				() -> assertFalse(questionAssignmentList.isEmpty()),
				() -> assertEquals(1, questionAssignmentList.size()),
				() -> assertTrue(questionAssignmentList.contains(questionAssingmentSaved))
		);
		
	}
	
	@Test
	@DisplayName("listQuestionAssignmentByQuestionAndAssignment return a Empty QuestionAssignmentList when question assignment not found")
	void listQuestionAssignmentByQuestionAndAssignment_ReturnEmptyQuestionAssignmentList_WhenQuestionAssignmentNotFound() {
		List<QuestionAssignmentModel> questionAssignmentList = questionAssignmentRepository
				.listQuestionAssignmentByQuestionAndAssignment(1L, 1L, professorToSave.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(questionAssignmentList),
				() -> assertTrue(questionAssignmentList.isEmpty())
		);
	}
	
	@Test
	@DisplayName("deleteById update a questionAssignment enabled field to false when successful")
	void deleteById_UpdateQuestionAssignmentEnabledFieldToFalse_WhenSuccsseful() {
		QuestionAssignmentModel questionAssignmentSave = insertQuestionAssingment();
		
		assertDoesNotThrow(() -> questionAssignmentRepository.deleteById(questionAssignmentSave.getIdQuestionAssignment()));
		
		Optional<QuestionAssignmentModel> findOneQuestionAssignment = questionAssignmentRepository
				.findOneQuestionAssignment(questionAssignmentSave.getIdQuestionAssignment(),
				professorToSave.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(findOneQuestionAssignment),
				() -> assertTrue(findOneQuestionAssignment.isEmpty())
		);
	}
	
	@Test
	@DisplayName("deleteAllQuestionAssignmentRelatedToCourse update a questionAssignment enabled field to false when successful")
	void deleteAllQuestionAssignmentRelatedToCourse_UpdateQuestionAssignmentEnabledFieldToFalse_WhenSuccsseful() {
		QuestionAssignmentModel questionAssignmentSave = insertQuestionAssingment();
		
		assertDoesNotThrow(() -> questionAssignmentRepository
				.deleteAllQuestionAssignmentRelatedToCourse(questionAssignmentSave.getQuestion().getCourse().getIdCourse(), 
						professorToSave.getIdProfessor()));
		
		Optional<QuestionAssignmentModel> findOneQuestionAssignment = questionAssignmentRepository
				.findOneQuestionAssignment(questionAssignmentSave.getIdQuestionAssignment(),
				professorToSave.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(findOneQuestionAssignment),
				() -> assertTrue(findOneQuestionAssignment.isEmpty())
		);
	}
	
	@Test
	@DisplayName("deleteAllQuestionAssignmentRelatedToAssignment update a questionAssignment enabled field to false when successful")
	void deleteAllQuestionAssignmentRelatedToAssignment_UpdateQuestionAssignmentEnabledFieldToFalse_WhenSuccsseful() {
		QuestionAssignmentModel questionAssignmentSave = insertQuestionAssingment();
		
		assertDoesNotThrow(() -> questionAssignmentRepository
				.deleteAllQuestionAssignmentRelatedToAssignment(questionAssignmentSave.getAssignment().getIdAssignment(), 
						professorToSave.getIdProfessor()));
		
		Optional<QuestionAssignmentModel> findOneQuestionAssignment = questionAssignmentRepository
				.findOneQuestionAssignment(questionAssignmentSave.getIdQuestionAssignment(),
						professorToSave.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(findOneQuestionAssignment),
				() -> assertTrue(findOneQuestionAssignment.isEmpty())
		);
	}
	
	@Test
	@DisplayName("deleteAllQuestionAssignmentRelatedToQuestion update a questionAssignment enabled field to false when successful")
	void deleteAllQuestionAssignmentRelatedToQuestion_UpdateQuestionAssignmentEnabledFieldToFalse_WhenSuccsseful() {
		QuestionAssignmentModel questionAssignmentSave = insertQuestionAssingment();
		
		assertDoesNotThrow(() -> questionAssignmentRepository
				.deleteAllQuestionAssignmentRelatedToQuestion(questionAssignmentSave.getQuestion().getIdQuestion(), 
						professorToSave.getIdProfessor()));
		
		Optional<QuestionAssignmentModel> findOneQuestionAssignment = questionAssignmentRepository
				.findOneQuestionAssignment(questionAssignmentSave.getIdQuestionAssignment(),
						professorToSave.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(findOneQuestionAssignment),
				() -> assertTrue(findOneQuestionAssignment.isEmpty())
		);
	}
	
	public QuestionAssignmentModel insertQuestionAssingment() {
		return questionAssignmentRepository.save(expectedQuestionAssignment);
	}
	
	public void insertValues() {
//		QuestionModel questionSave;
//		ChoiceModel choice1;
		roleRepository.save(RoleCreator.mockRoleProfessor());
		professorToSave = professorRepository.save(ProfessorCreator.mockProfessor());
		applicationUserRepository.save(ApplicationUserCreator.mockUserProfessor());
		courseRepository.save(CourseCreator.mockCourse());
		
		ChoiceModel choice2 = ChoiceModel.builder().idChoice(2L).title("Churrasco").correctAnswer(false)
				.question(QuestionCreator.mockQuestion()).professor(ProfessorCreator.mockProfessor()).enabled(true)
				.build();

		ChoiceModel choice3 = ChoiceModel.builder().idChoice(3L).title("Lasanha").correctAnswer(false)
				.question(QuestionCreator.mockQuestion()).professor(ProfessorCreator.mockProfessor()).enabled(true)
				.build();
		
		questionRepository.save(QuestionCreator.mockQuestion());
		choiceRepository.save(ChoiceCreator.mockChoice());
		choiceRepository.save(choice2);
		choiceRepository.save(choice3);
		
		//questionSave.setChoices(List.of(choice1, choice2, choice3));
		
		assignmentRepository.save(AssignmentCreator.mockAssignment());
	}
	
}
