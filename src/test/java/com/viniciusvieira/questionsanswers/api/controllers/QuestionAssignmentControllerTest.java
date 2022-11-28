package com.viniciusvieira.questionsanswers.api.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.viniciusvieira.questionsanswers.api.representation.models.QuestionAssignmentDto;
import com.viniciusvieira.questionsanswers.domain.exception.AssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionAssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionAssignmetAlreadyExistsException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.QuestionAssignmentModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.services.CascadeDeleteService;
import com.viniciusvieira.questionsanswers.domain.services.QuestionAssignmentService;
import com.viniciusvieira.questionsanswers.util.QuestionAssignmentCreator;
import com.viniciusvieira.questionsanswers.util.QuestionCreator;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Test for question assignment controller")
class QuestionAssignmentControllerTest {
	@MockBean
	private QuestionAssignmentService questionAssignmentServiceMock;
	@MockBean
	private CascadeDeleteService cascadeDeleteServiceMock;
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private final String url = "/api/professor/course/assignment/questionassignment";
	private String token;
	private QuestionAssignmentModel expectedQuestionAssignment;
	private List<QuestionAssignmentModel> expectedQuestionAssignmentList;
	
	@BeforeEach
	void setUp() throws Exception {
		expectedQuestionAssignment = QuestionAssignmentCreator.mockQuestionAssignment();
		expectedQuestionAssignmentList = List.of(expectedQuestionAssignment);
		
		String body = "{\"username\":\"vinicius\",\"password\":\"devdojo\"}";
		token = "Bearer " + testRestTemplate.postForEntity("/api/login", body, String.class).getBody();
		
		// findQuestionById
		BDDMockito.when(questionAssignmentServiceMock.findQuestionById(anyLong(), anyLong()))
				.thenReturn(List.of(QuestionCreator.mockQuestion()));
		
		// listByAssignment
		BDDMockito.when(questionAssignmentServiceMock.listByAssignment(anyLong()))
				.thenReturn(expectedQuestionAssignmentList);
		
		// save
		BDDMockito.when(questionAssignmentServiceMock.save(any(QuestionAssignmentDto.class)))
				.thenReturn(expectedQuestionAssignment);
		
		// replace
		BDDMockito.doNothing().when(questionAssignmentServiceMock).replace(anyLong(),
				any(QuestionAssignmentDto.class));
		
		// deleteQuestionAssignmentAndAllRelatedEntities
		BDDMockito.doNothing().when(cascadeDeleteServiceMock).deleteQuestionAssignmentAndAllRelatedEntities(anyLong());
				
			
	}
	
	public HttpEntity<Void> getValidAuthentication(){
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, this.token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Void>(headers);
	}

	@Test
	@DisplayName("findQuestionById return a questionList when successful")
	void findQuestionById_ReturnQuestionList_WhenSuccessful() {
		ParameterizedTypeReference<List<QuestionModel>> typeReference = new ParameterizedTypeReference<List<QuestionModel>>() {
		};
		
		ResponseEntity<List<QuestionModel>> exchange = testRestTemplate.exchange(url + "/list/1/1",
				HttpMethod.GET, getValidAuthentication(), typeReference);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
				() -> assertEquals(1, exchange.getBody().size())
		);
	}
	
	@Test
	@DisplayName("findQuestionById return status code 404 when question not found")
	void findQuestionById_Return404_WhenQuestionNotFound() {
		BDDMockito.when(questionAssignmentServiceMock.findQuestionById(anyLong(), anyLong()))
				.thenThrow(QuestionNotFoundException.class);
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/list/1/1",
				HttpMethod.GET, getValidAuthentication(), Object.class);
		
		System.out.println();
		System.out.println(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("listByAssignment return a questionAssignmentList when successful")
	void listByAssignment_ReturnQuestionAssignment_WhenSuccessful() {
		ParameterizedTypeReference<List<QuestionAssignmentModel>> typeReference = new ParameterizedTypeReference<List<QuestionAssignmentModel>>() {
		};
		
		ResponseEntity<List<QuestionAssignmentModel>> exchange = testRestTemplate.exchange(url + "/list/1",
				HttpMethod.GET, getValidAuthentication(), typeReference);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
				() -> assertTrue(exchange.getBody().contains(expectedQuestionAssignment))
		);
	}
	
	@Test
	@DisplayName("listByAssignment return status code 404 when questionAssignment not found")
	void listByAssignment_Return404_WhenQuestionAssignmentNotFound() {
		BDDMockito.when(questionAssignmentServiceMock.listByAssignment(anyLong()))
				.thenThrow(QuestionAssignmentNotFoundException.class);
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/list/1",
				HttpMethod.GET, getValidAuthentication(), Object.class);
		
		System.out.println();
		System.out.println(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("save insert a new questionAssignment when successful")
	void save_InsertNewQuestionAssignment_WhenSuccessful() {
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDto();
		HttpEntity<QuestionAssignmentDto> httpEntity = new HttpEntity<>(questionAssignmentDto,
				getValidAuthentication().getHeaders());
		
		ResponseEntity<QuestionAssignmentModel> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
				httpEntity, QuestionAssignmentModel.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.CREATED, exchange.getStatusCode()),
				() -> assertEquals(expectedQuestionAssignment, exchange.getBody())
		);
	}
	
	@Test
	@DisplayName("save return status code 400 when questionAssignment to save have invalid fields")
	void save_Return400_WhenQuestionAssignmentToSaveHaveInvalidFields() {
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDtoInvalid();
		HttpEntity<QuestionAssignmentDto> httpEntity = new HttpEntity<>(questionAssignmentDto,
				getValidAuthentication().getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
				httpEntity, Object.class);
		
		System.out.println();
		System.out.println(exchange.getBody() + "\n");
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
		);
	}
	
	// ? Retorna o body vazio
	@Test
	@DisplayName("save return status code 304 when questionAssignment association already exists")
	void save_Return304_WhenQuestionAssignmentAssociationAlreadyExists() {
		BDDMockito.when(questionAssignmentServiceMock.save(any(QuestionAssignmentDto.class)))
				.thenThrow(QuestionAssignmetAlreadyExistsException.class);
		
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDto();
		HttpEntity<QuestionAssignmentDto> httpEntity = new HttpEntity<>(questionAssignmentDto,
				getValidAuthentication().getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
				httpEntity, Object.class);
		
		System.out.println();
		System.out.println(exchange.getStatusCode() + "\n");
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NOT_MODIFIED, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("save return status code 404 when question not found")
	void save_Return404_WhenQuestionNotFound() {
		BDDMockito.when(questionAssignmentServiceMock.save(any(QuestionAssignmentDto.class)))
				.thenThrow(QuestionNotFoundException.class);
		
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDto();
		HttpEntity<QuestionAssignmentDto> httpEntity = new HttpEntity<>(questionAssignmentDto,
				getValidAuthentication().getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
				httpEntity, Object.class);
		
		System.out.println();
		System.out.println(exchange.getBody() + "\n");
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("save return status code 404 when assignment not found")
	void save_Return404_WhenAssignmentNotFound() {
		BDDMockito.when(questionAssignmentServiceMock.save(any(QuestionAssignmentDto.class)))
				.thenThrow(AssignmentNotFoundException.class);
		
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDto();
		HttpEntity<QuestionAssignmentDto> httpEntity = new HttpEntity<>(questionAssignmentDto,
				getValidAuthentication().getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
				httpEntity, Object.class);
		
		System.out.println();
		System.out.println(exchange.getBody() + "\n");
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace update questionAssignment when successful")
	void save_UpdateQuestionAssignment_WhenSuccessful() {
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDto();
		HttpEntity<QuestionAssignmentDto> httpEntity = new HttpEntity<>(questionAssignmentDto,
				getValidAuthentication().getHeaders());
		
		ResponseEntity<Void> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT,
				httpEntity, Void.class);
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace return 400 when questionAssignment to update have invalid fields")
	void save_Return400_WhenQuestionAssignmentToUpdateHaveInvalidFields() {
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDtoInvalid();
		HttpEntity<QuestionAssignmentDto> httpEntity = new HttpEntity<>(questionAssignmentDto,
				getValidAuthentication().getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT,
				httpEntity, Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace return 404 when questionAssignment not found")
	void save_Return404_WhenQuestionAssignmentNotFound() {
		BDDMockito.doThrow(QuestionAssignmentNotFoundException.class).when(questionAssignmentServiceMock)
				.replace(anyLong(), any(QuestionAssignmentDto.class));
		
		QuestionAssignmentDto questionAssignmentDto = QuestionAssignmentCreator.mockQuestionAssignmentDto();
		HttpEntity<QuestionAssignmentDto> httpEntity = new HttpEntity<>(questionAssignmentDto,
				getValidAuthentication().getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT,
				httpEntity, Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("deleteQuestionAssignmentAndAllRelatedEntities remove questionAssignment when successful")
	void deleteQuestionAssignmentAndAllRelatedEntities_RemoveQuestionAssignment_WhenSuccessful() {
		ResponseEntity<Void> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.DELETE,
				getValidAuthentication(), Void.class);
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("deleteQuestionAssignmentAndAllRelatedEntities return 404 when questionAssignment not found")
	void deleteQuestionAssignmentAndAllRelatedEntities_Return404_WhenQuestionAssignmentNotFound() {
		BDDMockito.doThrow(QuestionAssignmentNotFoundException.class).when(cascadeDeleteServiceMock)
				.deleteQuestionAssignmentAndAllRelatedEntities(anyLong());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.DELETE,
				getValidAuthentication(), Object.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}

}
