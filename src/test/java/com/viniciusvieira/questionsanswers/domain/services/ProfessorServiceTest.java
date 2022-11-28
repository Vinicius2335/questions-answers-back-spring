package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.domain.exception.ProfessorNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ProfessorRepository;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for professor service")
class ProfessorServiceTest {
    @InjectMocks
    private ProfessorService professorService;

    @Mock
    private ProfessorRepository professorRepositoryMock;

    private ProfessorModel expectedProfessor;

    @BeforeEach
    void setUp() {
        expectedProfessor = ProfessorCreator.mockProfessor();

        BDDMockito.when(professorRepositoryMock.findById(anyLong())).thenReturn(Optional.of(expectedProfessor));
    }

    @Test
    @DisplayName("findByIdOrThrowProfessorNotFoundException return a professor when successful")
    void findByIdOrThrowProfessorNotFoundException_ReturnProfessor_WhenSuccessful() {
        ProfessorModel professorFound = professorService
                .findByIdOrThrowProfessorNotFoundException(expectedProfessor.getIdProfessor());

        assertAll(
                () -> assertNotNull(professorFound),
                () -> assertEquals(expectedProfessor, professorFound)
        );
    }

    @Test
    @DisplayName("findByIdOrThrowProfessorNotFoundException throw professorNotFoundException when professor not found")
    void findByIdOrThrowProfessorNotFoundException_ThrownProfessorFoundException_WhenProfessorNotFound() {
        BDDMockito.when(professorRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProfessorNotFoundException.class,
                () -> professorService.findByIdOrThrowProfessorNotFoundException(99L));
    }
}