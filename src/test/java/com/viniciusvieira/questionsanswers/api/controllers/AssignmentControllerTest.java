package com.viniciusvieira.questionsanswers.api.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.viniciusvieira.questionsanswers.api.representation.models.AssignmentDto;
import com.viniciusvieira.questionsanswers.domain.exception.AssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;
import com.viniciusvieira.questionsanswers.domain.services.AssignmentService;
import com.viniciusvieira.questionsanswers.domain.services.CascadeDeleteService;
import com.viniciusvieira.questionsanswers.util.AssignmentCreator;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("Test for assignment controller")
class AssignmentControllerTest {
	@MockBean
	private AssignmentService assignmentServiceMock;
	@MockBean
	private CascadeDeleteService cascadeDeleteServiceMock;
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private AssignmentModel expectedAssignment;
	private List<AssignmentModel> expectedAssignmentList;
	private final String url = "/api/professor/course/assignment";
	private String token;

	@BeforeEach
	void setUp() {
		expectedAssignment = AssignmentCreator.mockAssignment();
		expectedAssignmentList = List.of(expectedAssignment);
		
		String body = "{\"username\":\"vinicius\",\"password\":\"devdojo\"}";
		token = "Bearer " + testRestTemplate.postForEntity("/api/login", body, String.class).getBody();
		
		// findAssignmentOrThrowsAssignmentNotFoundException
		BDDMockito.when(assignmentServiceMock.findAssignmentOrThrowsAssignmentNotFoundException(anyLong()))
				.thenReturn(expectedAssignment);
		
		// findByCourseAndTitle
		BDDMockito.when(assignmentServiceMock.findByCourseAndTitle(anyLong(), anyString()))
				.thenReturn(expectedAssignmentList);
		
		// save
		BDDMockito.when(assignmentServiceMock.save(any(AssignmentDto.class))).thenReturn(expectedAssignment);
		
		// delete
		//BDDMockito.doNothing().when(assignmentServiceMock).delete(anyLong());
		BDDMockito.doNothing().when(cascadeDeleteServiceMock).deleteAssignmentAndAllRelatedEntities(anyLong());
		
		// replace
		BDDMockito.doNothing().when(assignmentServiceMock).replace(anyLong(), any(AssignmentDto.class));
	}
	
	public HttpEntity<Void> getValidAuthentication(){
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, this.token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(headers);
	}

	@Test
	@DisplayName("getAssignmentById return a assignment when successful")
	void getAssignmentById_ReturnAssignment_WhenSuccessful() {
		ResponseEntity<AssignmentModel> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.GET,
				getValidAuthentication(), AssignmentModel.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
				() -> assertEquals(expectedAssignment, exchange.getBody())
		);
		
	}
	
	@Test
	@DisplayName("getAssignmentById return status code 404 when assignment not found")
	void getAssignmentById_Return404_WhenAssignmentNotFound() {
		BDDMockito.when(assignmentServiceMock.findAssignmentOrThrowsAssignmentNotFoundException(anyLong()))
				.thenThrow(AssignmentNotFoundException.class);
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.GET,
				getValidAuthentication(), Object.class);
		
		System.out.println();
		System.out.println(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("findByCourseAndTitle return a assignment when successful")
	void findByCourseAndTitle_ReturnAssignment_WhenSuccessful() {
		 ParameterizedTypeReference<List<AssignmentModel>> typeReference = new ParameterizedTypeReference<>() {
		};
		
		ResponseEntity<List<AssignmentModel>> exchange = testRestTemplate.exchange(url + "/list/1/?title=",
				HttpMethod.GET, getValidAuthentication(), typeReference);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
				() -> assertEquals(expectedAssignmentList, exchange.getBody())
		);
	}
	
	@Test
	@DisplayName("findByCourseAndTitle return status code 404 when assignment not found")
	void findByCourseAndTitle_Return404_WhenAssignmentNotFound() {
		BDDMockito.when(assignmentServiceMock.findByCourseAndTitle(anyLong(), anyString()))
				.thenReturn(List.of());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/list/1/?title=",
				HttpMethod.GET, getValidAuthentication(), Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("findByCourseAndTitle return status code 404 when course not found")
	void findByCourseAndTitle_Return404_WhenCourseNotFound() {
		BDDMockito.when(assignmentServiceMock.findByCourseAndTitle(anyLong(), anyString()))
				.thenThrow(CourseNotFoundException.class);
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/list/1/?title=",
				HttpMethod.GET, getValidAuthentication(), Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("save insert a assignment when successful")
	void save_InsertAssignment_WhenSuccessful() {
		AssignmentDto assignmentDto = AssignmentCreator.mockAssignmentDto();
		HttpEntity<AssignmentDto> httpEntity = new HttpEntity<>(assignmentDto, getValidAuthentication()
				.getHeaders());
		
		ResponseEntity<AssignmentModel> exchange = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity,
				AssignmentModel.class);
		
		System.out.println();
		System.out.println(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.CREATED, exchange.getStatusCode()),
				() -> assertEquals(expectedAssignment, exchange.getBody())
		);
	}
	
	@Test
	@DisplayName("save return status code 404 when course not found")
	void save_Rturn404_WhenCourseNotFound() {
		BDDMockito.when(assignmentServiceMock.save(any(AssignmentDto.class))).thenThrow(CourseNotFoundException.class);
		
		AssignmentDto assignmentDto = AssignmentCreator.mockAssignmentDto();
		HttpEntity<AssignmentDto> httpEntity = new HttpEntity<>(assignmentDto, getValidAuthentication()
				.getHeaders());
		
		ResponseEntity<AssignmentModel> exchange = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity,
				AssignmentModel.class);
		
		System.out.println();
		System.out.println(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("save return status code 400 when assignment fields invalid")
	void save_Rturn400_WhenAssignmentFieldsInvalid() {
		AssignmentDto assignmentDto = AssignmentCreator.mockAssignmentDtoInvalid();
		HttpEntity<AssignmentDto> httpEntity = new HttpEntity<>(assignmentDto, getValidAuthentication()
				.getHeaders());
		
		ResponseEntity<AssignmentModel> exchange = testRestTemplate.exchange(url, HttpMethod.POST, httpEntity,
				AssignmentModel.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("delete remove a assignment when successul")
	void delete_RemoveAssignment_WhenSuccessful() {
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.DELETE,
				getValidAuthentication(), Object.class);
		
		System.out.println();
		System.out.println(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("delete return 404 when assignment not found")
	void delete_Return404_WhenAssignmentNotFound() {
		//BDDMockito.doNothing().when(assignmentServiceMock).delete(anyLong());
		BDDMockito.doThrow(AssignmentNotFoundException.class).when(cascadeDeleteServiceMock)
				.deleteAssignmentAndAllRelatedEntities(anyLong());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.DELETE,
				getValidAuthentication(), Object.class);
		
		System.out.println();
		System.out.println(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace update a assignment when successful")
	void replace_UpdateAssignment_WhenSuccessful() {
		AssignmentDto assignmentDto = AssignmentCreator.mockAssignmentDtoToUpdate();
		HttpEntity<AssignmentDto> httpEntity = new HttpEntity<>(assignmentDto, getValidAuthentication()
				.getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT, httpEntity,
				Object.class);
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace return status code 400 when assignmentdto have invalid fields")
	void replace_Return400_WhenAssignmentDtoHaveInvalidFields() {
		AssignmentDto assignmentDto = AssignmentCreator.mockAssignmentDtoInvalid();
		HttpEntity<AssignmentDto> httpEntity = new HttpEntity<>(assignmentDto, getValidAuthentication()
				.getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT, httpEntity,
				Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace return status code 404 when assignment not found")
	void replace_Return404_WhenAssignmentNotFound() {
		BDDMockito.doThrow(AssignmentNotFoundException.class).when(assignmentServiceMock).replace(anyLong(),
				any(AssignmentDto.class));
		
		AssignmentDto assignmentDto = AssignmentCreator.mockAssignmentDtoToUpdate();
		HttpEntity<AssignmentDto> httpEntity = new HttpEntity<>(assignmentDto, getValidAuthentication()
				.getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT, httpEntity,
				Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace return status code 404 when course not found")
	void replace_Return404_WhenCouseNotFound() {
		BDDMockito.doThrow(CourseNotFoundException.class).when(assignmentServiceMock).replace(anyLong(),
				any(AssignmentDto.class));
		
		AssignmentDto assignmentDto = AssignmentCreator.mockAssignmentDtoToUpdate();
		HttpEntity<AssignmentDto> httpEntity = new HttpEntity<>(assignmentDto, getValidAuthentication()
				.getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT, httpEntity,
				Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}

}
