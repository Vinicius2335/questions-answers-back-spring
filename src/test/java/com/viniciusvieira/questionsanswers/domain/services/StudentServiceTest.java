package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.api.mappers.v2.ApplicationUserMapper;
import com.viniciusvieira.questionsanswers.api.mappers.v2.StudentMapper;
import com.viniciusvieira.questionsanswers.api.representation.models.ApplicationUserDto;
import com.viniciusvieira.questionsanswers.api.representation.models.StudentDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.ApplicationUserRequestBody;
import com.viniciusvieira.questionsanswers.api.representation.requests.StudentRequestBody;
import com.viniciusvieira.questionsanswers.domain.exception.StudentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.domain.models.StudentModel;
import com.viniciusvieira.questionsanswers.domain.repositories.StudentRepository;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.StudentCreator;
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
@DisplayName("Test for student service")
class StudentServiceTest {
    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepositoryMock;
    @Mock
    private InsertUserService insertUserServiceMock;
    @Mock
    private DeleteUserService deleteUserServiceMock;
    @Mock
    private StudentMapper studentMapperMock;
    @Mock
    private ApplicationUserMapper applicationUserMapperMock;

    private StudentModel expectedStudent;
    private StudentDto expectedStudentDto;
    private ApplicationUserModel expectedUserStudent;
    private ApplicationUserDto expectedApplicationUserDto;
    private List<StudentModel> expectedStudents;
    private List<StudentDto> expectedStudentDtoList;

    @BeforeEach
    void setUp() {
        expectedStudent = StudentCreator.mockStudent();
        expectedStudentDto = StudentCreator.mockStudentDto();
        expectedStudents = List.of(expectedStudent);
        expectedStudentDtoList = List.of(expectedStudentDto);
        expectedUserStudent = ApplicationUserCreator.mockUserStudent();
        expectedApplicationUserDto = ApplicationUserCreator.mockApplicationUserDto();

        // findByNameContaining
        BDDMockito.when(studentRepositoryMock.findByNameContaining(anyString())).thenReturn(expectedStudents);
        // save
        BDDMockito.when(studentRepositoryMock.save(any())).thenReturn(expectedStudent);
        // findById
        BDDMockito.when(studentRepositoryMock.findById(anyLong())).thenReturn(Optional.of(expectedStudent));
        // deleteById
        BDDMockito.doNothing().when(studentRepositoryMock).deleteById(anyLong());

        // studentMapper - toListOfStudentDto
        BDDMockito.when(studentMapperMock.toListOfStudentDto(anyList())).thenReturn(expectedStudentDtoList);
        // studentMapper - toStudentDomain
        BDDMockito.when(studentMapperMock.toStudentDomain(any(StudentDto.class))).thenReturn(expectedStudent);
        // studentMapper - toStudentDomain
        BDDMockito.when(studentMapperMock.toStudentDomain(any(StudentRequestBody.class))).thenReturn(expectedStudent);
        // studentMapper - toStudentDto
        BDDMockito.when(studentMapperMock.toStudentDto(any(StudentModel.class))).thenReturn(expectedStudentDto);

        // applicationUserMapperMock - toApplicationUserDto
        BDDMockito.when(applicationUserMapperMock.toApplicationUserDto(any(ApplicationUserModel.class)))
                .thenReturn(ApplicationUserCreator.mockApplicationUserDto());

        // insertUserServiceMock - insertStudent
        BDDMockito.when(insertUserServiceMock.insertStudent(any(StudentModel.class),
                any(ApplicationUserRequestBody.class))).thenReturn(expectedUserStudent);

        // deleteUserServiceMock - delete Student
        BDDMockito.doNothing().when(deleteUserServiceMock).deleteStudent(any());
    }

    @Test
    @DisplayName("findByName return student list when successful")
    void findByName_ReturnStudentList_WhenSuccessful() {
        List<StudentDto> students = studentService.findByName(expectedStudent.getName());

        assertAll(
                () -> assertNotNull(students),
                () -> assertFalse(students.isEmpty()),
                () -> assertEquals(1, students.size()),
                () -> assertEquals(expectedStudentDtoList, students)
        );
    }

    @Test
    @DisplayName("findByName return empty student list when student not found by name")
    void findByName_ReturnEmptyStudentList_WhenStudentNotFoundByName() {
        BDDMockito.when(studentRepositoryMock.findByNameContaining(anyString())).thenReturn(List.of());
        BDDMockito.when(studentMapperMock.toListOfStudentDto(anyList())).thenReturn(List.of());

        List<StudentDto> students = studentService.findByName(expectedStudent.getName());

        assertAll(
                () -> assertNotNull(students),
                () -> assertTrue(students.isEmpty())
        );
    }

    @Test
    @DisplayName("saveStudent insert student when successful")
    void saveStudent_InsertStudent_WhenSuccessful() {
        StudentDto studentSaved = studentService.saveStudent(StudentCreator.mockStudentRequestBody());

        assertAll(
                () -> assertNotNull(studentSaved),
                () -> assertEquals(expectedStudentDto, studentSaved)
        );
    }

    @Test
    @DisplayName("saveApplicationUserStudent insert application user student when successful")
    void saveApplicationUserStudent_InsertApplicationUserStudent_WhenSuccessful() {
        ApplicationUserRequestBody userToSaved = ApplicationUserCreator.mockApplicationUserRequestBody();
        ApplicationUserDto userSaved = studentService.saveApplicationUserStudent(1L, userToSaved);

        assertAll(
                () -> assertNotNull(userSaved),
                () -> assertEquals(expectedApplicationUserDto.getIdApplicationUser(), userSaved.getIdApplicationUser()),
                () -> assertEquals(expectedApplicationUserDto.getUsername(), userSaved.getUsername())
        );
    }

    @Test
    @DisplayName("saveApplicationUserStudent throws StudentNotFoundException when student not found")
    void saveApplicationUserStudent_ThrowsStudentNotFoundException_WhenStudentNotFound() {
        BDDMockito.when(studentRepositoryMock.findById(anyLong())).thenThrow(StudentNotFoundException.class);

        ApplicationUserRequestBody userToSaved = ApplicationUserCreator.mockApplicationUserRequestBody();

        assertThrows(StudentNotFoundException.class, () -> studentService.saveApplicationUserStudent(1L,
                userToSaved) );
    }

    @Test
    @DisplayName("replace update student when successful")
    void replace_UpdatStudent_WhenSuccessful() {
        StudentRequestBody studentToSaved = StudentCreator.mockStudentRequestBody();
        StudentDto studentUpdated = studentService.replace(1L, studentToSaved);

        assertAll(
                () -> assertNotNull(studentUpdated),
                () -> assertEquals(expectedStudentDto, studentUpdated)
        );
    }

    @Test
    @DisplayName("replace throws StudentNotFoundException when student not found")
    void replace_ThrowsStudentNotFoundException_WhenStudentNotFound() {
        BDDMockito.when(studentRepositoryMock.findById(anyLong())).thenThrow(StudentNotFoundException.class);

        StudentRequestBody studentToSaved = StudentCreator.mockStudentRequestBody();

        assertThrows(StudentNotFoundException.class, () -> studentService.replace(1L,
                studentToSaved));
    }

    @Test
    @DisplayName("delete remove student when successful")
    void delete_RemoveStudent_WhenSuccessful() {
        assertDoesNotThrow(() -> studentService.delete(1L));
    }

    @Test
    @DisplayName("delete throws StudentNotFoundException when student not found")
    void delete_ThrowsStudentNotFoundException_WhenStudentNotFound() {
        BDDMockito.when(studentRepositoryMock.findById(anyLong())).thenThrow(StudentNotFoundException.class);

        assertThrows(StudentNotFoundException.class, () -> studentService.delete(1L));
    }
}