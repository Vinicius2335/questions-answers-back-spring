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
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.ChoiceCreator;
import com.viniciusvieira.questionsanswers.util.CourseCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import com.viniciusvieira.questionsanswers.util.QuestionCreator;
import com.viniciusvieira.questionsanswers.util.RoleCreator;

@DataJpaTest
@DisplayName("Test for choice repository")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ChoiceRepositoryTest {
	@Autowired
	private ChoiceRepository choiceRepository;
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
	
	private ProfessorModel professorSaved;
	private QuestionModel questionSaved;
	private ChoiceModel choiceToSave;

	@BeforeEach
	void setUp() {
		roleRepository.save(RoleCreator.mockRoleProfessor());
		professorSaved = professorRepository.save(ProfessorCreator.mockProfessor());
		applicationUserRepository.save(ApplicationUserCreator.mockUserProfessor());
		courseRepository.save(CourseCreator.mockCourse());
		questionSaved = questionRepository.save(QuestionCreator.mockQuestion());
		
		choiceToSave = ChoiceCreator.mockChoice();
	}

	@Test
	@DisplayName("save insert choice when successful")
	void save_InsertChoice_WhenSuccessful() {
		ChoiceModel choiceSaved = choiceRepository.save(choiceToSave);
		
		assertAll(
				() -> assertNotNull(choiceSaved),
				() -> assertEquals(choiceToSave, choiceSaved)
		);
	}
	
	@Test
	@DisplayName("listChoiceByQuestionId return a choice list when successful")
	void listChoiceByQuestionId_ReturnChoiceList_WhenSuccessful() {
		ChoiceModel choiceSaved = choiceRepository.save(choiceToSave);
		
		List<ChoiceModel> choiceList = choiceRepository.listChoiceByQuestionId(questionSaved.getIdQuestion(),
				professorSaved.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(choiceList),
				() -> assertFalse(choiceList.isEmpty()),
				() -> assertTrue(choiceList.contains(choiceSaved)),
				() -> assertEquals(1, choiceList.size())
		);
	}
	
	@Test
	@DisplayName("listChoiceByQuestionId return a empty choice list when choice not found")
	void listChoiceByQuestionId_ReturnEmptyChoiceList_WhenChoiceNotFound() {
		List<ChoiceModel> choiceList = choiceRepository.listChoiceByQuestionId(questionSaved.getIdQuestion(),
				professorSaved.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(choiceList),
				() -> assertTrue(choiceList.isEmpty())
		);
	}
	
	@Test
	@DisplayName("findOneChoice return a optional choice when successful")
	void findOneChoice_ReturnOptionalChoice_WhenSuccessful() {
		ChoiceModel choiceSaved = choiceRepository.save(choiceToSave);
		
		Optional<ChoiceModel> choiceFound = choiceRepository.findOneChoice(choiceSaved.getIdChoice(),
				professorSaved.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(choiceFound),
				() -> assertFalse(choiceFound.isEmpty()),
				() -> assertEquals(choiceSaved, choiceFound.get())
		);
	}
	
	@Test
	@DisplayName("findOneChoice return a empty optional choice when choice not found")
	void findOneChoice_ReturnEmptyChoice_WhenChoiceNotFound() {
		Optional<ChoiceModel> choiceFound = choiceRepository.findOneChoice(1L,
				professorSaved.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(choiceFound),
				() -> assertTrue(choiceFound.isEmpty())
		);
	}
	
	@Test
	@DisplayName("deleteById remove choice when successful")
	void deleteById_RemoveChoice_WhenSuccessful() {
		ChoiceModel choiceSaved = choiceRepository.save(choiceToSave);
		
		assertDoesNotThrow(() -> choiceRepository.deleteById(choiceSaved.getIdChoice(),
				professorSaved.getIdProfessor()));
	}
	
	@Test
	@DisplayName("updatedAllOtherChoicesCorrectAnswerToFalse change choice field correct_answer to false when"
			+ " successful")
	void updatedAllOtherChoicesCorrectAnswerToFalse_ChangeCoiceFieldCorrectAnswerToFalse_WhenSuccessful() {
		ChoiceModel choiceSaved = choiceRepository.save(choiceToSave);
		ChoiceModel choiceSaved2 = ChoiceModel.builder().idChoice(2L).title("E AE")
				.correctAnswer(true).professor(professorSaved).question(questionSaved).enabled(true).build();
		choiceRepository.save(choiceSaved2);
		
		choiceRepository.updatedAllOtherChoicesCorrectAnswerToFalse(choiceSaved, questionSaved,
				professorSaved.getIdProfessor());
		
		List<ChoiceModel> choiceListFound = choiceRepository.testUpdatedAllOtherChoicesCorrectAnswerToFalse();
		
		assertFalse(choiceListFound.contains(choiceSaved));
	}
	
	@Test
	@DisplayName("deleteAllChoicesRelatedToQuestion remove all choices related to question")
	void deleteAllChoicesRelatedToQuestion_RemoveAllChoice_WhenRelatedToQuestion() {
		choiceRepository.save(choiceToSave);
		ChoiceModel choiceSaved2 = ChoiceModel.builder().idChoice(2L).title("E AE")
				.correctAnswer(true).professor(professorSaved).question(questionSaved).enabled(true).build();
		choiceRepository.save(choiceSaved2);
		
		choiceRepository.deleteAllChoicesRelatedToQuestion(questionSaved.getIdQuestion(),
				professorSaved.getIdProfessor());
		
		List<ChoiceModel> findAll = choiceRepository.testFindAllEnabledFalse();
		System.out.println("test " + findAll);
		
		assertAll(
				() -> assertNotNull(findAll),
				() -> assertFalse(findAll.isEmpty()),
				() -> assertEquals(2, findAll.size())
		);
	}
	
	@Test
	@DisplayName("deleteAllChoicesRelatedToCourse remove all choices related to course")
	void deleteAllChoicesRelatedToCourse_RemoveAllChoice_WhenRelatedToCourse() {
		choiceRepository.save(choiceToSave);
		ChoiceModel choiceSaved2 = ChoiceModel.builder().idChoice(2L).title("E AE")
				.correctAnswer(true).professor(professorSaved).question(questionSaved).enabled(true).build();
		choiceRepository.save(choiceSaved2);
		
		choiceRepository.deleteAllChoicesRelatedToCourse(questionSaved.getCourse().getIdCourse(),
				professorSaved.getIdProfessor());
		
		List<ChoiceModel> findAll = choiceRepository.testFindAllEnabledFalse();
		System.out.println("test " + findAll);
		
		assertAll(
				() -> assertNotNull(findAll),
				() -> assertFalse(findAll.isEmpty()),
				() -> assertEquals(2, findAll.size())
		);
	}
	
	@Test
	@DisplayName("listChoiceByQuestionAndTitle return a list of choices when successful")
	void listChoiceByQuestionAndTitle_ReturnListChoice_WhenSuccessful() {
		ChoiceModel choiceSaved = choiceRepository.save(choiceToSave);
		
		List<ChoiceModel> listChoiceByQuestionAndTitle = choiceRepository.listChoiceByQuestionAndTitle(choiceSaved.
				getQuestion().getIdQuestion(), choiceSaved.getTitle(), professorSaved.getIdProfessor());
		
		
		assertAll(
				() -> assertNotNull(listChoiceByQuestionAndTitle),
				() -> assertEquals(1, listChoiceByQuestionAndTitle.size()),
				() -> assertFalse(listChoiceByQuestionAndTitle.isEmpty()),
				() -> assertTrue(listChoiceByQuestionAndTitle.contains(choiceSaved))
		);
	}
	
	@Test
	@DisplayName("listChoiceByQuestionAndTitle return a empty list of choices when choice not found")
	void listChoiceByQuestionAndTitle_ReturnEmptyListChoice_WhenChoiceNotFound() {
		List<ChoiceModel> listChoiceByQuestionAndTitle = choiceRepository.listChoiceByQuestionAndTitle(1L,
				"Olá?", professorSaved.getIdProfessor());
		
		
		assertAll(
				() -> assertNotNull(listChoiceByQuestionAndTitle),
				() -> assertTrue(listChoiceByQuestionAndTitle.isEmpty())
		);
	}
	
}
