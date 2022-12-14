package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.domain.exception.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.repositories.CourseRepository;
import com.viniciusvieira.questionsanswers.util.CourseCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@Log4j2
@ExtendWith(SpringExtension.class)
@DisplayName("Test for course service")
class CourseServiceTest {
    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepositoryMock;
    @Mock
    private ExtractEntityFromTokenService extractEntityFromTokenServiceMock;

    private CourseModel courseToSave;
    private List<CourseModel> expectedListCourse;
    private ProfessorModel professorMock;

    @BeforeEach
    void setUp() throws Exception {
        courseToSave = CourseCreator.mockCourse();
        expectedListCourse = List.of(courseToSave);
        professorMock = ProfessorCreator.mockProfessor();

        // findOneCourse
        BDDMockito.when(courseRepositoryMock.findOneCourse(anyLong(), anyLong()))
                .thenReturn(Optional.of(courseToSave));

        // ExtractEntityFromTokenService - extractProfessorFromToken
        BDDMockito.when(extractEntityFromTokenServiceMock.extractProfessorFromToken())
                .thenReturn(professorMock);

        // listCoursesByName
        BDDMockito.when(courseRepositoryMock.listCoursesByName(anyString(), anyLong()))
                .thenReturn(expectedListCourse);

        // save
        BDDMockito.when(courseRepositoryMock.save(any(CourseModel.class))).thenReturn(courseToSave);

        // deleteById
        BDDMockito.doNothing().when(courseRepositoryMock).deleteById(anyLong(), anyLong());

    }

    @Test
    @DisplayName("findByIdOrThrowCourseNotFoundException return a course when successful")
    void findByIdOrThrowCourseNotFoundException_ReturnCourse_WhenSuccessful() {
        CourseModel courseFound = courseService.findByIdOrThrowCourseNotFoundException(1L);

        assertAll(
                () -> assertNotNull(courseFound),
                () -> assertEquals(courseToSave, courseFound)
        );
    }

    @Test
    @DisplayName("findByIdOrThrowCourseNotFoundException throw CourseNotFoundException when course not found")
    void findByIdOrThrowCourseNotFoundException_ThrowCourseNotFoundException_WhenCourseNotFound() {
        BDDMockito.when(courseRepositoryMock.findOneCourse(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () ->
                courseService.findByIdOrThrowCourseNotFoundException(1L));
    }

    @Test
    @DisplayName("findByName return a list of course when successful")
    void findByName_ReturnListCourse_WhenSuccessful() {
        List<CourseModel> courseList = courseService.findByName(courseToSave.getName());

        assertAll(
                () -> assertNotNull(courseList),
                () -> assertEquals(1, courseList.size()),
                () -> assertTrue(courseList.contains(courseToSave))
        );
    }

    @Test
    @DisplayName("findByName return a empty list of course when course not found")
    void findByName_ReturnEmptyListCourse_WhenCourseNotFound() {
        BDDMockito.when(courseRepositoryMock.listCoursesByName(anyString(), anyLong()))
                .thenReturn(List.of());

        List<CourseModel> courseList = courseService.findByName(courseToSave.getName());

        assertTrue(courseList.isEmpty());
    }

    @Test
    @DisplayName("save insert and return a course when successful")
    void save_InsertAndReturnCourse_WhenSuccessful() {
        CourseModel courseSaved = courseService.save(CourseCreator.mockCourseDto());

        log.info("TESTE {}", courseSaved);

        assertAll(
                () -> assertNotNull(courseSaved),
                () -> assertEquals(courseToSave, courseSaved)
        );
    }

    @Test
    @DisplayName("save Throw ConstraintViolationException when courseSto have invalid fields")
    void save_ThrowConstraintViolationException_WhenCourseDtoHaveInvalidFields() {
        BDDMockito.when(courseRepositoryMock.save(any(CourseModel.class)))
                .thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class,
                () -> courseService.save(CourseCreator.mockInvalidCourseDto()));
    }

    @Test
    @DisplayName("replace updated course when successful")
    void replace_UpdatedCourse_WhenSuccessful() {
        CourseModel courseToUpdate = CourseCreator.mockCourseUpdated();
        BDDMockito.when(courseRepositoryMock.save(any(CourseModel.class))).thenReturn(courseToUpdate);

        assertDoesNotThrow(() -> courseService.replace(courseToUpdate.getIdCourse(),
                CourseCreator.mockCourseDtoToUpdated()));
    }

    @Test
    @DisplayName("replace throw CourseNotFoundException when course not found")
    void replace_ThrowCourseNotFoundException_WhenCourseNotFound() {
        BDDMockito.when(courseRepositoryMock.findOneCourse(anyLong(), anyLong()))
                .thenReturn(Optional.empty());
        CourseModel courseToUpdate = CourseCreator.mockCourseUpdated();

        assertThrows(CourseNotFoundException.class, () -> courseService.replace(courseToUpdate.getIdCourse(),
                CourseCreator.mockCourseDtoToUpdated()));
    }

    @Test
    @DisplayName("delete remove course when succesful")
    void delete_RemoveCourse_WhenSuccessful() {
        assertDoesNotThrow(() -> courseService.delete(1L));
    }

    @Test
    @DisplayName("delete throw CourseNotFoundException when course not found")
    void delete_ThrowCourseNotFoundException_WhenCourseNotFound() {
        BDDMockito.when(courseRepositoryMock.findOneCourse(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.delete(1L));
    }

}
