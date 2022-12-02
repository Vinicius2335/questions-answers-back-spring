package com.viniciusvieira.questionsanswers.domain.repositories;

import com.viniciusvieira.questionsanswers.domain.models.StudentModel;
import com.viniciusvieira.questionsanswers.util.RoleCreator;
import com.viniciusvieira.questionsanswers.util.StudentCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Test for Student Repository")
class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.save(RoleCreator.mockRoleStudent());
    }

    public StudentModel insertStudent(){
        StudentModel studentToSaved = StudentCreator.mockStudent();
        return  studentRepository.save(studentToSaved);
    }

    @Test
    @DisplayName("save insert a student when successful")
    void save_InsertStudent_WhenSuccessful(){
        StudentModel studentToSaved = StudentCreator.mockStudent();
        StudentModel studentSaved = studentRepository.save(studentToSaved);

        assertAll(
                () -> assertNotNull(studentSaved),
                () -> assertEquals(studentToSaved, studentSaved)
        );
    }

    @Test
    @DisplayName("findById return a Optional student when successful")
    void findById_ReturnOptionalStudent_WhenSuccessful(){
        StudentModel studentSaved = insertStudent();
        Optional<StudentModel> studentFound = studentRepository.findById(studentSaved.getIdStudent());

        assertAll(
                () -> assertNotNull(studentFound),
                () -> assertFalse(studentFound.isEmpty()),
                () -> assertEquals(studentSaved, studentFound.get())
        );
    }

    @Test
    @DisplayName("findById return a Empty Optional student when student not found")
    void findById_ReturnEmptyOptionalStudent_WhenStudentNotFound(){
        Optional<StudentModel> studentFound = studentRepository.findById(1L);

        assertTrue(studentFound.isEmpty());
    }

    @Test
    @DisplayName("findByNameContaining return a studentsList when successful")
    void findByNameContaining_ReturnStudentsList_WhenSuccessful(){
        StudentModel studentSaved = insertStudent();
        List<StudentModel> studentsFound = studentRepository.findByNameContaining(studentSaved.getName());

        assertAll(
                () -> assertNotNull(studentsFound),
                () -> assertFalse(studentsFound.isEmpty()),
                () -> assertEquals(1, studentsFound.size()),
                () -> assertTrue(studentsFound.contains(studentSaved))
        );
    }

    @Test
    @DisplayName("findByNameContaining return a Empty studentsList when student not found by name")
    void findByNameContaining_ReturnEmpyStudentsList_WhenStudentNotFoundByName(){
        List<StudentModel> studentsFound = studentRepository.findByNameContaining("vinicius");

        assertTrue(studentsFound.isEmpty());
    }

    @Test
    @DisplayName("deleteById remove student when successful")
    void deleteById_RemoveStudent_WhenSuccessful(){
        StudentModel studentSaved = insertStudent();

        studentRepository.deleteById(studentSaved.getIdStudent());
        List<StudentModel> students = studentRepository.findByNameContaining("");

        assertTrue(students.isEmpty());
    }

}