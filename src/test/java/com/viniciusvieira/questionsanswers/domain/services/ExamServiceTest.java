package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.domain.exception.ChoiceNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionAssignmentModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ChoiceRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.QuestionAssignmentRepository;
import com.viniciusvieira.questionsanswers.util.ChoiceCreator;
import com.viniciusvieira.questionsanswers.util.QuestionAssignmentCreator;
import com.viniciusvieira.questionsanswers.util.QuestionCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for exam service")
class ExamServiceTest {
    @InjectMocks
    private ExamService examService;

    @Mock
    private QuestionAssignmentRepository questionAssignmentRepositoryMock;
    @Mock
    private ChoiceRepository choiceRepositoryMock;

    private List<QuestionAssignmentModel> expectedQuestionAssignmentList;
    private List<ChoiceModel> expectedChoiceList;

    @BeforeEach
    void setUp() {
        expectedQuestionAssignmentList = List.of(QuestionAssignmentCreator.mockQuestionAssignment());
        expectedChoiceList = List.of(ChoiceCreator.mockChoice());

        // QuestionAssignmentRepository - listQuestionsFromQuestionsAssignmentByAssignmentAccessCode
        BDDMockito.when(questionAssignmentRepositoryMock
                .listQuestionsFromQuestionsAssignmentByAssignmentAccessCode(any()))
                .thenReturn(expectedQuestionAssignmentList);

        // ChoiceRepository - listChoiceByQuestionsIdForStudent
        BDDMockito.when(choiceRepositoryMock.listChoiceByQuestionsIdForStudent(anyList()))
                .thenReturn(expectedChoiceList);
    }

    @Test
    @DisplayName("findAllQuestionsByAccessCode return questionList when successful")
    void findAllQuestionsByAccessCode_ReturnQuestionList_WhenSuccessful() {
        List<QuestionModel> questions = examService.findAllQuestionsByAccessCode("1234");

        assertAll(
                () -> assertNotNull(questions),
                () -> assertFalse(questions.isEmpty()),
                () -> assertEquals(1, questions.size()),
                () -> assertTrue(questions.contains(QuestionCreator.mockQuestion()))
        );
    }

    @Test
    @DisplayName("findAllQuestionsByAccessCode throws QuestionNotFoundException when dont have questions" +
            " associated a assignment for access code")
    void findAllQuestionsByAccessCode_ThrowsQuestionNotFoundException_WhenDontHaveQuestionsAssociatedAssignmentForAccessCode() {
        BDDMockito.when(questionAssignmentRepositoryMock
                        .listQuestionsFromQuestionsAssignmentByAssignmentAccessCode(any()))
                .thenReturn(List.of());

        assertThrows(QuestionNotFoundException.class, () -> examService.findAllQuestionsByAccessCode("1234"));
    }

    @Test
    @DisplayName("findAllChoicesByAccessCode return choiceList when successful")
    void findAllChoicesByAccessCode_ReturnChoiceList_WhenSuccessful() {
        List<ChoiceModel> choices = examService.findAllChoicesByAccessCode("1234");

        assertAll(
                () -> assertNotNull(choices),
                () -> assertFalse(choices.isEmpty()),
                () -> assertEquals(1, choices.size()),
                () -> assertEquals(expectedChoiceList, choices),
                () -> assertTrue(choices.contains(ChoiceCreator.mockChoice()))
        );
    }

    @Test
    @DisplayName("findAllChoicesByAccessCode throws ChoiceNotFoundException when dont have choices associated a" +
            " assignment for access code")
    void findAllChoicesByAccessCode_ThrowsChoiceNotFoundException_WhenDontHaveChoicesAssociatedAssignmentForAccessCode() {
        BDDMockito.when(choiceRepositoryMock.listChoiceByQuestionsIdForStudent(anyList()))
                .thenReturn(List.of());

        assertThrows(ChoiceNotFoundException.class, () -> examService.findAllChoicesByAccessCode("1234"));
    }
}
