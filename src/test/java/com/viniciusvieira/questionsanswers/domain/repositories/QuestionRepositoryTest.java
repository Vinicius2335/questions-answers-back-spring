package com.viniciusvieira.questionsanswers.domain.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
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
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.AssignmentCreator;
import com.viniciusvieira.questionsanswers.util.ChoiceCreator;
import com.viniciusvieira.questionsanswers.util.CourseCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import com.viniciusvieira.questionsanswers.util.QuestionAssignmentCreator;
import com.viniciusvieira.questionsanswers.util.QuestionCreator;
import com.viniciusvieira.questionsanswers.util.RoleCreator;

import lombok.RequiredArgsConstructor;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Test for question repository")
@RequiredArgsConstructor
class QuestionRepositoryTest {
	@Autowired
	private QuestionRepository questionRepository;
	
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
	private QuestionAssignmentRepository questionAssignmentRepository;
	
	
	private QuestionModel questionToSave;
	private ProfessorModel professorSaved;
	private CourseModel courseSaved;

	@BeforeEach
	void setUp() throws Exception {
		questionToSave = QuestionCreator.mockQuestion();
		
		roleRepository.save(RoleCreator.mockRoleProfessor());
		professorSaved = professorRepository.save(ProfessorCreator.mockProfessor());
		System.out.println("TESTE " + professorSaved);
		applicationUserRepository.save(ApplicationUserCreator.mockUserProfessor());
		courseSaved = courseRepository.save(CourseCreator.mockCourse());
		
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
		assignmentRepository.save(AssignmentCreator.mockAssignment());
		questionAssignmentRepository.save(QuestionAssignmentCreator.mockQuestionAssignment());
	}

	@Test
	@DisplayName("save insert and retur question when successful")
	void save_InsertAndReturn_WhenSuccessful() {
		QuestionModel questionSaved = questionRepository.save(questionToSave);
		
		assertAll(
				() -> assertNotNull(questionSaved),
				() -> assertEquals(questionToSave, questionSaved)
		);
	}
	
	@Test
	@DisplayName("findOneQuestion return optional question when successful")
	void findOneQuestion_ReturnOptionQuestion_WhenSuccesful() {
		QuestionModel questionSaved = questionRepository.save(questionToSave);
		Optional<QuestionModel> questionFound = questionRepository.findOneQuestion(questionSaved.getIdQuestion(),
				professorSaved.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(questionFound),
				() -> assertFalse(questionFound.isEmpty()),
				() -> assertEquals(questionToSave, questionFound.get())
		);
	}
	
	@Test
	@DisplayName("findOneQuestion return empty optional question when question not found")
	void findOneQuestion_ReturnEmptyOptionQuestion_WhenQuestionNotFound() {
		Optional<QuestionModel> questionFound = questionRepository.findOneQuestion(1L,
				professorSaved.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(questionFound),
				() -> assertTrue(questionFound.isEmpty())
		);
	}
	
	@Test
	@DisplayName("listQuestionByCourseAndTitle return a list question when successful")
	void listQuestionByCourseAndTitle_ReturnListQuestion_WhenSuccessful() {
		QuestionModel questionSaved = questionRepository.save(questionToSave);
		List<QuestionModel> questionList = questionRepository.listQuestionByCourseAndTitle(questionSaved.getIdQuestion(), 
				questionSaved.getTitle(), professorSaved.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(questionList),
				() -> assertFalse(questionList.isEmpty()),
				() -> assertEquals(1, questionList.size()),
				() -> assertTrue(questionList.contains(questionSaved))
		);
	}
	
	@Test
	@DisplayName("listQuestionByCourseAndTitle return a empty list question when question not found")
	void listQuestionByCourseAndTitle_ReturnEmptyListQuestion_WhenQuestionNotFound() {
		List<QuestionModel> questionList = questionRepository.listQuestionByCourseAndTitle(1L, 
				"Zalerap", professorSaved.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(questionList),
				() -> assertTrue(questionList.isEmpty())
		);
	}
	
	@Test
	@DisplayName("deleteById update question enabled to false when successful")
	void deleteById_UpdateQuestionEnabledToFalse_WhenSuccessful() {
		QuestionModel questionSaved = questionRepository.save(questionToSave);
		questionRepository.deleteById(questionSaved.getIdQuestion(), professorSaved.getIdProfessor());
		
		Optional<QuestionModel> questionFound = questionRepository.findOneQuestion(questionSaved.getIdQuestion(),
				professorSaved.getIdProfessor());
		
		assertTrue(questionFound.isEmpty());
	}
	
	@Test
	@DisplayName("deleteAllQuestionsRelatedToCouse update question enabled to false when successful")
	void deleteAllQuestionsRelatedToCouse_UpdateQuestionEnabledToFalse_WhenSuccessful() {
		QuestionModel questionSaved = questionRepository.save(questionToSave);
		questionRepository.deleteAllQuestionsRelatedToCouse(courseSaved.getIdCourse(),
				professorSaved.getIdProfessor());
		
		Optional<QuestionModel> questionFound = questionRepository.findOneQuestion(questionSaved.getIdQuestion(),
				professorSaved.getIdProfessor());
		
		assertTrue(questionFound.isEmpty());
	}
	
	// TODO: Falta terminar
	@Test
	@DisplayName("findAllQuestionsByCourseNotAssociatedWithAnAssignment return a questionList when successful")
	void findAllQuestionsByCourseNotAssociatedWithAnAssignment_ReturnQuestionList_WhenSuccessful() {
		List<QuestionModel> questionList = questionRepository.findAllQuestionsByCourseNotAssociatedWithAnAssignment(1l, 1l, 1l);
		
		System.out.println();
		System.out.println(questionList);
		System.out.println();
		
		assertNotNull(questionList);
	}

}
