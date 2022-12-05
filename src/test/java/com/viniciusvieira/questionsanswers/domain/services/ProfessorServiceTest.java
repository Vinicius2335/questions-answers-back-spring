package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.api.mappers.v2.ApplicationUserMapper;
import com.viniciusvieira.questionsanswers.api.mappers.v2.ProfessorMapper;
import com.viniciusvieira.questionsanswers.api.representation.models.ApplicationUserDto;
import com.viniciusvieira.questionsanswers.api.representation.models.ProfessorDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.ApplicationUserRequestBody;
import com.viniciusvieira.questionsanswers.api.representation.requests.ProfessorRequestBody;
import com.viniciusvieira.questionsanswers.domain.exception.ProfessorNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ProfessorRepository;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for professor service")
class ProfessorServiceTest {
    @InjectMocks
    private ProfessorService professorService;

    @Mock
    private ProfessorRepository professorRepositoryMock;
    @Mock
    private ProfessorMapper professorMapperMock;
    @Mock
    private ApplicationUserMapper applicationUserMapperMock;
    @Mock
    private InsertUserService insertUserServiceMock;
    @Mock
    private DeleteUserService deleteUserServiceMock;

    private ProfessorModel expectedProfessor;
    private ProfessorDto expectedProfessorDto;
    private ProfessorRequestBody expectedProfessorRequestBody;
    private ApplicationUserModel expectedApplicationUserProfessor;
    private ApplicationUserRequestBody expectedApplicationUserRequestBody;
    private ApplicationUserDto expectedApplicationUserDto;

    private List<ProfessorModel> expectedProfessors;
    private List<ProfessorDto> expectedProfessorsDto;

    @BeforeEach
    void setUp() {
        expectedProfessor = ProfessorCreator.mockProfessor();
        expectedProfessorDto = ProfessorCreator.mockProfessorDto();
        expectedProfessorRequestBody = ProfessorCreator.mockProfessorRequestBody();
        expectedApplicationUserProfessor = ApplicationUserCreator.mockUserProfessor();
        expectedApplicationUserRequestBody = ApplicationUserCreator.mockApplicationUserRequestBody();
        expectedApplicationUserDto = ApplicationUserCreator.mockApplicationUserDto();

        expectedProfessors = List.of(expectedProfessor);
        expectedProfessorsDto = List.of(expectedProfessorDto);

        //findById
        BDDMockito.when(professorRepositoryMock.findById(anyLong())).thenReturn(Optional.of(expectedProfessor));
        //findByNameContaining
        BDDMockito.when(professorRepositoryMock.findByNameContaining(anyString())).thenReturn(expectedProfessors);
        //save
        BDDMockito.when(professorRepositoryMock.save(any(ProfessorModel.class))).thenReturn(expectedProfessor);
        // deleteById
        BDDMockito.doNothing().when(professorRepositoryMock).deleteById(anyLong());

        // professorMapperMock - toProfessorDtoList
        BDDMockito.when(professorMapperMock.toProfessorDtoList(anyList())).thenReturn(expectedProfessorsDto);
        // professorMapperMock - toProfessorDomain
        BDDMockito.when(professorMapperMock.toProfessorDomain(any(ProfessorRequestBody.class)))
                .thenReturn(expectedProfessor);
        // professorMapperMock - toProfessorDto
        BDDMockito.when(professorMapperMock.toProfessorDto(any(ProfessorModel.class))).thenReturn(expectedProfessorDto);

        // InsertUserService - insertProfessor
        BDDMockito.when(insertUserServiceMock.insertProfessor(any(ProfessorModel.class), any(ApplicationUserRequestBody.class)))
                .thenReturn(expectedApplicationUserProfessor);

        // DeleteUserService - deleteProfessor
        BDDMockito.doNothing().when(deleteUserServiceMock).deleteProfessor(any(ProfessorModel.class));

        // ApplicationUserMapper - toApplicationUserDto
        BDDMockito.when(applicationUserMapperMock.toApplicationUserDto(any(ApplicationUserModel.class)))
                .thenReturn(expectedApplicationUserDto);

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

    @Test
    @DisplayName("findByName return a professorList when successful")
    void findByName_ReturnProfessorList_WhenSuccessful() {
        List<ProfessorDto> professorsFound = professorService.findByName(expectedProfessor.getName());

        assertAll(
                () -> assertNotNull(professorsFound),
                () -> assertFalse(professorsFound.isEmpty()),
                () -> assertEquals(expectedProfessorsDto, professorsFound),
                () -> assertEquals(1, professorsFound.size())
        );
    }

    @Test
    @DisplayName("findByName return a empty professorList when professor not found by name")
    void findByName_ReturnEmptyProfessorList_WhenProfessorNotFoundByName() {
        BDDMockito.when(professorRepositoryMock.findByNameContaining(anyString())).thenReturn(List.of());
        BDDMockito.when(professorMapperMock.toProfessorDtoList(anyList())).thenReturn(List.of());

        List<ProfessorDto> professorsFound = professorService.findByName(expectedProfessor.getName());

        assertAll(
                () -> assertNotNull(professorsFound),
                () -> assertTrue(professorsFound.isEmpty())
        );
    }

    @Test
    @DisplayName("saveProfessor insert professor when successful")
    void saveProfessor_InsertProfessor_WhenSuccessful() {
        ProfessorDto professorSaved = professorService.saveProfessor(expectedProfessorRequestBody);

        assertAll(
                () -> assertNotNull(professorSaved),
                () -> assertEquals(expectedProfessorDto, professorSaved)
        );
    }

    @Test
    @DisplayName("saveApplicationUserProfessor insert application user professor when successful")
    void saveApplicationUserProfessor_InsertApplicationUserProfessor_WhenSuccessful() {
        ApplicationUserDto applicationUserProfessorSaved = professorService
                .saveApplicationUserProfessor(expectedProfessor.getIdProfessor(), expectedApplicationUserRequestBody);

        assertAll(
                () -> assertNotNull(applicationUserProfessorSaved),
                () -> assertEquals(expectedApplicationUserDto, applicationUserProfessorSaved)
        );
    }

    @Test
    @DisplayName("saveApplicationUserProfessor Throws ProfessorNotFoundException when professor not found by id")
    void saveApplicationUserProfessor_ThrowsProfessorNotFoundException_WhenProfessorNotFoundById() {
        BDDMockito.when(professorRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProfessorNotFoundException.class,
                () -> professorService.saveApplicationUserProfessor(expectedProfessor.getIdProfessor(),
                        expectedApplicationUserRequestBody));
    }

    @Test
    @DisplayName("replace update professor when successful")
    void replace_UpdateProfessor_WhenSuccessful() {
        ProfessorDto professorUpdated = professorService.replace(expectedProfessor.getIdProfessor(),
                expectedProfessorRequestBody);

        assertAll(
                () -> assertNotNull(professorUpdated),
                () -> assertEquals(expectedProfessorDto, professorUpdated)
        );
    }

    @Test
    @DisplayName("replace Throws ProfessorNotFoundException when professor not found by id")
    void replace_ThrowsProfessorNotFoundException_WhenProfessorNotFoundById() {
        BDDMockito.when(professorRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProfessorNotFoundException.class,
                () -> professorService.replace(expectedProfessor.getIdProfessor(),
                        expectedProfessorRequestBody));
    }

    @Test
    @DisplayName("delete remove professor when successful")
    void delete_RemoveProfessor_WhenSuccessful() {
        assertDoesNotThrow(() -> professorService.delete(expectedProfessor.getIdProfessor()));
    }

    @Test
    @DisplayName("delete Throws ProfessorNotFoundException when professor not found by id")
    void delete_ThrowsProfessorNotFoundException_WhenProfessorNotFoundById() {
        BDDMockito.when(professorRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProfessorNotFoundException.class,
                () -> professorService.delete(expectedProfessor.getIdProfessor()));
    }
}