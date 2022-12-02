package com.viniciusvieira.questionsanswers.domain.services;

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
@DisplayName("Test for delete user service")
class DeleteUserServiceTest {
    @InjectMocks
    private DeleteUserService deleteUserService;

    @Mock
    private ApplicationUserRepository applicationUserRepositoryMock;

    private StudentModel studentModel;
    private ProfessorModel professorModel;
    private ApplicationUserModel expectedApplicationUserStudent;
    private ApplicationUserModel expectedApplicationUserProfessor;

    @BeforeEach
    void setUp() {
        studentModel = StudentCreator.mockStudent();
        professorModel = ProfessorCreator.mockProfessor();

        expectedApplicationUserProfessor = ApplicationUserCreator.mockUserProfessor();
        expectedApplicationUserStudent = ApplicationUserCreator.mockUserStudent();

        BDDMockito.when(applicationUserRepositoryMock.findByStudent(any(StudentModel.class)))
                .thenReturn(expectedApplicationUserStudent);

        BDDMockito.when(applicationUserRepositoryMock.findByProfessor(any(ProfessorModel.class)))
                .thenReturn(expectedApplicationUserProfessor);
    }

    @Test
    @DisplayName("deleteStudent remove application user student when successful")
    void deleteStudent_RemoveApplicationUserStudent_WhenSuccessful() {
        assertDoesNotThrow(() -> deleteUserService.deleteStudent(studentModel));
    }

    @Test
    @DisplayName("deleteStudent remove application user professor when successful")
    void deleteProfessor_RemoveApplicationUserProfessor_WhenSuccessful() {
        assertDoesNotThrow(() -> deleteUserService.deleteProfessor(professorModel));
    }
}