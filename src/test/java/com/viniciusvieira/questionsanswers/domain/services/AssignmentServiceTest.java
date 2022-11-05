package com.viniciusvieira.questionsanswers.domain.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.viniciusvieira.questionsanswers.api.representation.models.AssignmentDto;
import com.viniciusvieira.questionsanswers.domain.excepiton.AssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.excepiton.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.AssignmentRepository;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.AssignmentCreator;
import com.viniciusvieira.questionsanswers.util.CourseCreator;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for assignment service")
class AssignmentServiceTest {
	@InjectMocks
	private AssignmentService assignmentService;

	@Mock
	private AssignmentRepository assignmentRepositoryMock;
	@Mock
	private ApplicationUserRepository applicationUserRepositoryMock;
	@Mock CourseService courseServiceMock;

	private AssignmentModel expectedAssignment;
	private List<AssignmentModel> expectedAssignmentList;

	@BeforeEach
	void setUp() throws Exception {
		expectedAssignment = AssignmentCreator.mockAssignment();
		expectedAssignmentList = List.of(expectedAssignment);
		
		// extractProfessorFromToken
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
				
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when((String)authentication.getPrincipal())
				.thenReturn(ApplicationUserCreator.mockUserProfessor().getUsername());
				
		BDDMockito.when(applicationUserRepositoryMock.findByUsername(anyString()))
				.thenReturn(ApplicationUserCreator.mockUserProfessor());
		
		// findOneAssignment
		BDDMockito.when(assignmentRepositoryMock.findOneAssignment(anyLong(), anyLong()))
				.thenReturn(Optional.of(expectedAssignment));
		
		// course findByIdOrThrowCourseNotFoundException
		BDDMockito.when(courseServiceMock.findByIdOrThrowCourseNotFoundException(anyLong()))
				.thenReturn(CourseCreator.mockCourse());
		
		// listAssignmentByCourseAndTitle
		BDDMockito.when(assignmentRepositoryMock.listAssignmentByCourseAndTitle(anyLong(), anyString(), anyLong()))
				.thenReturn(expectedAssignmentList);
		
		// save
		BDDMockito.when(assignmentRepositoryMock.save(any(AssignmentModel.class)))
				.thenReturn(expectedAssignment);
		
		// deleteById
		BDDMockito.doNothing().when(assignmentRepositoryMock).deleteById(anyLong(), anyLong());
		
		// deleteAllAssignmentRelatedToCourse
		BDDMockito.doNothing().when(assignmentRepositoryMock)
				.deleteAllAssignmentRelatedToCourse(anyLong(), anyLong());
				
	}

	@Test
	@DisplayName("findAssignmentOrThrowsAssignmentNotFoundException return a assignment when successful")
	void findAssignmentOrThrowsAssignmentNotFoundException_ReturnAssignment_WhenSuccessful() {
		AssignmentModel assignmentFound = assignmentService.findAssignmentOrThrowsAssignmentNotFoundException(1L);
		
		assertAll(
				() -> assertNotNull(assignmentFound),
				() -> assertEquals(expectedAssignment, assignmentFound)
		);
	}
	
	@Test
	@DisplayName("findAssignmentOrThrowsAssignmentNotFoundException throws AssignmentNotFoundException when assignment not found")
	void findAssignmentOrThrowsAssignmentNotFoundException_ThrowsAssignmentNotFoundException_WhenAssignmentNotFound() {
		BDDMockito.when(assignmentRepositoryMock.findOneAssignment(anyLong(), anyLong()))
				.thenReturn(Optional.empty());
		
		assertThrows(AssignmentNotFoundException.class, () -> assignmentService
				.findAssignmentOrThrowsAssignmentNotFoundException(1L));
	}
	
	@Test
	@DisplayName("findByCourseAndTitle return a assignment when successful")
	void findByCourseAndTitle_ReturnAssignment_WhenSuccessful() {
		List<AssignmentModel> assignmentFoundList = assignmentService.findByCourseAndTitle(expectedAssignment
				.getCourse().getIdCourse(), expectedAssignment.getTitle());
		
		assertAll(
				() -> assertNotNull(assignmentFoundList),
				() -> assertFalse(assignmentFoundList.isEmpty()),
				() -> assertEquals(1, assignmentFoundList.size()),
				() -> assertTrue(assignmentFoundList.contains(expectedAssignment))
		);
	}
	
	@Test
	@DisplayName("findByCourseAndTitle Throws CourseNotFoundException when course not found")
	void findByCourseAndTitle_ThrowsCourseNotFoundException_WhenCouserNotFound() {
		BDDMockito.when(courseServiceMock.findByIdOrThrowCourseNotFoundException(anyLong()))
				.thenThrow(CourseNotFoundException.class);
		
		assertThrows(CourseNotFoundException.class, () -> assignmentService.findByCourseAndTitle(expectedAssignment
				.getCourse().getIdCourse(), expectedAssignment.getTitle()));
	}
	
	@Test
	@DisplayName("save insert a assignment when successful")
	void save_InsertAssignment_WhenSuccessful() {
		AssignmentDto assignmentDto = AssignmentCreator.mockAssignmentDto();
		AssignmentModel assignmentSaved = assignmentService.save(assignmentDto);
		
		assertAll(
				() -> assertNotNull(assignmentSaved),
				() -> assertEquals(expectedAssignment, assignmentSaved)
		);
	}
	
	@Test
	@DisplayName("save Throws CourseNotFoundException when course not found")
	void save_ThrowsCourseNotFoundException_WhenCouserNotFound() {
		BDDMockito.when(courseServiceMock.findByIdOrThrowCourseNotFoundException(anyLong()))
				.thenThrow(CourseNotFoundException.class);
		
		AssignmentDto assignmentDto = AssignmentCreator.mockAssignmentDto();
		
		assertThrows(CourseNotFoundException.class, () -> assignmentService.save(assignmentDto));
	}
	
	@Test
	@DisplayName("replace Updated a assignment when successful")
	void replace_UpdatedAssignment_WhenSuccessful() {
		AssignmentDto assignmentDto = AssignmentCreator.mockAssignmentDto();
		assertDoesNotThrow(() -> assignmentService.replace(1L, assignmentDto));
	}
	
	@Test
	@DisplayName("replace Throws CourseNotFoundException when course not found")
	void replace_ThrowsCourseNotFoundException_WhenCouserNotFound() {
		BDDMockito.when(courseServiceMock.findByIdOrThrowCourseNotFoundException(anyLong()))
			.thenThrow(CourseNotFoundException.class);
		
		AssignmentDto assignmentDto = AssignmentCreator.mockAssignmentDto();
		
		assertThrows(CourseNotFoundException.class, () -> assignmentService.replace(1L, assignmentDto));
	}
	
	@Test
	@DisplayName("replace throws AssignmentNotFoundException when assignment not found")
	void replace_ThrowsAssignmentNotFoundException_WhenAssignmentNotFound() {
		BDDMockito.when(assignmentRepositoryMock.findOneAssignment(anyLong(), anyLong()))
				.thenReturn(Optional.empty());
		
		AssignmentDto assignmentDto = AssignmentCreator.mockAssignmentDto();
		
		assertThrows(AssignmentNotFoundException.class, () -> assignmentService.replace(1L, assignmentDto));
	}
	
	@Test
	@DisplayName("delete remove a assignment when successful")
	void delete_RemoveAssignment_WhenSuccessful() {
		assertDoesNotThrow(() -> assignmentService.delete(1L));
	}
	
	@Test
	@DisplayName("delete throws AssignmentNotFoundException when assignment not found")
	void delete_ThrowsAssignmentNotFoundException_WhenAssignmentNotFound() {
		BDDMockito.when(assignmentRepositoryMock.findOneAssignment(anyLong(), anyLong()))
				.thenReturn(Optional.empty());
		
		assertThrows(AssignmentNotFoundException.class, () -> assignmentService.delete(1L));
	}
	
	@Test
	@DisplayName("deleteAllAssignmentRelatedToCourse remove all assigment related to course when successful")
	void deleteAllAssignmentRelatedToCourse_RemoveAllAssignmentRelatedToCourse_WhenSuccessful() {
		assertDoesNotThrow(() -> assignmentService.deleteAllAssignmentRelatedToCourse(1l));
	}

}
