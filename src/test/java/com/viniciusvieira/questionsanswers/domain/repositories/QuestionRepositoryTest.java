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

import com.viniciusvieira.questionsanswers.domain.models.CourseModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.CourseRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.ProfessorRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.QuestionRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.RoleRepository;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.CourseCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import com.viniciusvieira.questionsanswers.util.QuestionCreator;
import com.viniciusvieira.questionsanswers.util.RoleCreator;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Test for question repository")
class QuestionRepositoryTest {
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private ProfessorRepository professorRepository;
	@Autowired
	private ApplicationUserRepository applicationUserRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private QuestionRepository questionRepository;
	
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

}
