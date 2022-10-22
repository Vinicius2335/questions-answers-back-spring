package com.viniciusvieira.questionsanswers.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.viniciusvieira.questionsanswers.models.CourseModel;
import com.viniciusvieira.questionsanswers.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.CourseCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import com.viniciusvieira.questionsanswers.util.RoleCreator;

@DataJpaTest
@DisplayName("Test for course repository")
class CourseRepositoryTest {
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private ProfessorRepository professorRepository;
	@Autowired
	private ApplicationUserRepository applicationUserRepository;
	@Autowired
	private RoleRepository roleRepository;
	private CourseModel courseToSave;
	private ProfessorModel professorSaved;
	private List<CourseModel> expectedListCourse;
	
	@BeforeEach
	void setUp() throws Exception {
		roleRepository.save(RoleCreator.mockRoleProfessor());
		professorSaved = professorRepository.save(ProfessorCreator.mockProfessor());
		applicationUserRepository.save(ApplicationUserCreator.mockUserProfessor());
		courseToSave = CourseCreator.mockCourse();
		expectedListCourse = List.of(courseToSave);
	}

	@Test
	@DisplayName("save insert and return a course when successful")
	void save_ReturnCourse_WhenSuccessful() {
		CourseModel courseSaved = courseRepository.save(courseToSave);
		
		assertAll(
				() -> assertNotNull(courseSaved),
				() -> assertEquals(courseToSave, courseSaved)
		);
	}
	
	@Test
	@DisplayName("findOneCourse return Course when successful")
	void findOneCourse_ReturnCourse_WhenSuccessful() {
		CourseModel courseSaved = courseRepository.save(courseToSave);
		
		Optional<CourseModel> courseFound = courseRepository.findOneCourse(courseSaved.getIdCourse(),
				professorSaved.getIdProfessor());
		
		assertAll(
				() -> assertFalse(courseFound.isEmpty()),
				() -> assertNotNull(courseFound.get()),
				() -> assertEquals(courseSaved, courseFound.get())
		);
	}
	
	@Test
	@DisplayName("findOneCourse return Empty when course not found")
	void findOneCourse_ReturnEmpty_WhenCourseNotFound() {
		Optional<CourseModel> courseFound = courseRepository.findOneCourse(1L, professorSaved.getIdProfessor());
		
		assertTrue(courseFound.isEmpty());
	}
	
	@Test
	@DisplayName("listCoursesByName return a list of course when successful")
	void listCoursesByName_ReturnListCourse_WhenSuccessful() {
		CourseModel courseSaved = courseRepository.save(courseToSave);
		
		List<CourseModel> listCoursesByName = courseRepository.listCoursesByName(courseSaved.getName(),
				professorSaved.getIdProfessor());
		
		assertAll(
				() -> assertNotNull(listCoursesByName),
				() -> assertEquals(1, listCoursesByName.size()),
				() -> assertEquals(expectedListCourse, listCoursesByName)
		);
	}
		
		@Test
		@DisplayName("listCoursesByName return a empty list of course when course not found by name")
		void listCoursesByName_ReturnEmptyListCourse_WhenCourseNotFoundByName() {
			List<CourseModel> listCoursesByName = courseRepository.listCoursesByName("Java",
					professorSaved.getIdProfessor());
			
			assertTrue(listCoursesByName.isEmpty());
	}
	
	@Test
	@DisplayName("deleteById update course enabled to false when successful")
	void deleteById_RemoveCourseById_WhenSuccessful() {
		CourseModel courseSaved = courseRepository.save(courseToSave);
		courseRepository.deleteById(courseSaved.getIdCourse(), professorSaved.getIdProfessor());
		
		Optional<CourseModel> findOneCourse = courseRepository.findOneCourse(courseSaved.getIdCourse(),
				professorSaved.getIdProfessor());
		
		assertTrue(findOneCourse.isEmpty());
	}
}
