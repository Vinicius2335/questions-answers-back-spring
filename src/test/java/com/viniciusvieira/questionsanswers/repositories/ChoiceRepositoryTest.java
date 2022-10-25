package com.viniciusvieira.questionsanswers.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.viniciusvieira.questionsanswers.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.models.QuestionModel;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.ChoiceCreator;
import com.viniciusvieira.questionsanswers.util.CourseCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import com.viniciusvieira.questionsanswers.util.QuestionCreator;
import com.viniciusvieira.questionsanswers.util.RoleCreator;

@DataJpaTest
@DisplayName("Test for choice repository")
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
	void setUp() throws Exception {
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
	
}