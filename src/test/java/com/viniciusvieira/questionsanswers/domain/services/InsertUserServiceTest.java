package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.api.representation.requests.ApplicationUserRequestBody;
import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.StudentModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import com.viniciusvieira.questionsanswers.util.StudentCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for insert user service")
class InsertUserServiceTest {
    @InjectMocks
    private InsertUserService insertUserService;

    @Mock
    private ApplicationUserRepository applicationUserRepositoryMock;

    private ApplicationUserModel expectedApplicationUserStudent;
    private ApplicationUserModel expectedApplicationUserProfessor;
    private StudentModel studentModel;
    private ProfessorModel professorModel;
    private ApplicationUserRequestBody applicationUserRequestBody;

    @BeforeEach
    void setUp() {
        expectedApplicationUserProfessor = ApplicationUserCreator.mockUserProfessor();
        expectedApplicationUserStudent = ApplicationUserCreator.mockUserStudent();

        studentModel = StudentCreator.mockStudent();
        professorModel = ProfessorCreator.mockProfessor();
        applicationUserRequestBody = ApplicationUserCreator.mockApplicationUserRequestBody();
    }

    @Test
    @DisplayName("insertStudent insert applicationUser student when successful")
    void insertStudent_InsertpplicationUserStudent_WhenSuccessful() {
        BDDMockito.when(applicationUserRepositoryMock.save(any())).thenReturn(expectedApplicationUserStudent);

        ApplicationUserModel userSaved = insertUserService.insertStudent(studentModel, applicationUserRequestBody);

        assertAll(
                () -> assertNotNull(userSaved),
                () -> assertEquals(expectedApplicationUserStudent, userSaved)
        );
    }

    @Test
    @DisplayName("insertProfessor insert applicationUser professor when successful")
    void insertProfessor_InsertpplicationUserProfessor_WhenSuccessful() {
        BDDMockito.when(applicationUserRepositoryMock.save(any())).thenReturn(expectedApplicationUserProfessor);

        ApplicationUserModel userSaved = insertUserService.insertProfessor(professorModel, applicationUserRequestBody);

        assertAll(
                () -> assertNotNull(userSaved),
                () -> assertEquals(expectedApplicationUserProfessor, userSaved)
        );
    }
}