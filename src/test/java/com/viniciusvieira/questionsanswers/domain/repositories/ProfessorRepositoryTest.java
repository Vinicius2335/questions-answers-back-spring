package com.viniciusvieira.questionsanswers.domain.repositories;

import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import com.viniciusvieira.questionsanswers.util.RoleCreator;
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
@DisplayName("Test for Professor Repository")
class ProfessorRepositoryTest {
    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.save(RoleCreator.mockRoleProfessor());
    }

    ProfessorModel insertProfessor(){
        ProfessorModel professorToSave = ProfessorCreator.mockProfessor();
        return professorRepository.save(professorToSave);
    }

    @Test
    @DisplayName("save insert professor when successful")
    void save_InsertProfessor_WhenSuccessful(){
        ProfessorModel professorToSave = ProfessorCreator.mockProfessor();
        ProfessorModel professorSaved = professorRepository.save(professorToSave);

        assertAll(
                () -> assertNotNull(professorSaved),
                () -> assertEquals(professorToSave, professorSaved)
        );
    }

    @Test
    @DisplayName("findByEmail return a professor when successful")
    void findByEmail_ReturnProfessor_WhenSuccessful() {
        ProfessorModel professorSaved = insertProfessor();
        ProfessorModel professorFound = professorRepository.findByEmail(professorSaved.getEmail());

        assertAll(
                () -> assertNotNull(professorFound),
                () -> assertEquals(professorSaved, professorFound)
        );
    }

    @Test
    @DisplayName("findByEmail return a null professor when professor not found")
    void findByEmail_ReturnNullProfessor_WhenProfessorNotFound() {
        ProfessorModel professorFound = professorRepository.findByEmail("professor123@email");

        assertNull(professorFound);
    }

    @Test
    @DisplayName("findByNameContaining return a professorList when successufl")
    void findByNameContaining_ReturnProfessorList_WhenSuccessful(){
        ProfessorModel professorSaved = insertProfessor();
        List<ProfessorModel> professors = professorRepository.findByNameContaining(professorSaved.getName());

        assertAll(
                () -> assertNotNull(professors),
                () -> assertEquals(1, professors.size()),
                () -> assertTrue(professors.contains(professorSaved))
        );
    }

    @Test
    @DisplayName("findByNameContaining return a Empty professorList when professor not found")
    void findByNameContaining_ReturnEmptyProfessorList_WhenProfessorNotFound(){
        List<ProfessorModel> professors = professorRepository.findByNameContaining("");

       assertTrue(professors.isEmpty());
    }

    @Test
    @DisplayName("findById return a Optional professor when successful")
    void findById_ReturnOptionalProfessor_WhenSuccessful(){
        ProfessorModel professorSaved = insertProfessor();
        Optional<ProfessorModel> professorFound = professorRepository.findById(professorSaved.getIdProfessor());

        assertAll(
                () -> assertNotNull(professorFound),
                () -> assertFalse(professorFound.isEmpty()),
                () -> assertEquals(professorSaved, professorFound.get())
        );
    }

    @Test
    @DisplayName("findById return a empty optional professor when professor not found")
    void findById_ReturnEmptyOptionalProfessor_WhenProfessorNotFound(){
        Optional<ProfessorModel> professorFound = professorRepository.findById(99L);

        assertAll(
                () -> assertNotNull(professorFound),
                () -> assertTrue(professorFound.isEmpty())
        );
    }

    @Test
    @DisplayName("deleteById remove professor when successful")
    void deleteById_RemoveProfessor_WhenSuccessful(){
        ProfessorModel professor = insertProfessor();

        professorRepository.deleteById(professor.getIdProfessor());
        List<ProfessorModel> professorsFound = professorRepository.findByNameContaining(professor.getName());

        assertTrue(professorsFound.isEmpty());
    }
}