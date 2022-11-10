package com.viniciusvieira.questionsanswers.api.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.viniciusvieira.questionsanswers.api.representation.models.ChoiceDto;
import com.viniciusvieira.questionsanswers.domain.excepiton.ChoiceNotFoundException;
import com.viniciusvieira.questionsanswers.domain.excepiton.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.services.ChoiceService;
import com.viniciusvieira.questionsanswers.util.ChoiceCreator;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Test for choice controller")
class ChoiceControllerTest {
	@MockBean
	private ChoiceService choiceServiceMock;
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private final String url = "/api/professor/course/question/choice";
	private String token;
	private ChoiceModel choiceToSave;
	private List<ChoiceModel> expectedChoiceList;

	@BeforeEach
	void setUp() throws Exception {
		choiceToSave = ChoiceCreator.mockChoice();
		expectedChoiceList = List.of(choiceToSave);
		
		String body = "{\"username\":\"vinicius\",\"password\":\"devdojo\"}";
		token = "Bearer " + testRestTemplate.postForEntity("/api/login", body, String.class).getBody();
		
		// listChoiceByQuestion
		BDDMockito.when(choiceServiceMock.listChoiceByQuestion(anyLong())).thenReturn(expectedChoiceList);
		
		// getChoiceByIdOrThrowChoiceNotFoundException
		BDDMockito.when(choiceServiceMock.getChoiceByIdOrThrowChoiceNotFoundException(anyLong()))
				.thenReturn(choiceToSave);
		
		// findByQuestionAndTitle
		BDDMockito.when(choiceServiceMock.findByQuestionAndTitle(anyLong(), anyString()))
				.thenReturn(expectedChoiceList);
		
		// save
		BDDMockito.when(choiceServiceMock.save(any(ChoiceDto.class))).thenReturn(choiceToSave);
		
		// replace
		BDDMockito.doNothing().when(choiceServiceMock).replace(anyLong(), any(ChoiceDto.class));
		
		// delete
		BDDMockito.doNothing().when(choiceServiceMock).delete(anyLong());
	}
	
	public HttpEntity<Void> getValidAuthentication(){
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, this.token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Void>(headers);
	}

	@Test
	@DisplayName("getListChoices return a list of choices when successful")
	void getListChoices_ReturnListChoices_WhenSuccessful() {
		ParameterizedTypeReference<List<ChoiceModel>> typeReference = new ParameterizedTypeReference<List<ChoiceModel>>() {
		};
		
		ResponseEntity<List<ChoiceModel>> exchange = testRestTemplate.exchange(url + "/list/1", HttpMethod.GET,
				getValidAuthentication(), typeReference);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertFalse(exchange.getBody().isEmpty()),
				() -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
				() -> assertTrue(exchange.getBody().contains(choiceToSave))
		);
	}
	
	@Test
	@DisplayName("getListChoices return status code 404 when choice not found")
	void getListChoices_Return404_WhenCourseNotFound() {
		BDDMockito.when(choiceServiceMock.listChoiceByQuestion(anyLong())).thenReturn(List.of());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/list/1", HttpMethod.GET,
				getValidAuthentication(), Object.class);
		
		log.info(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("getChoiceById return choice when successful")
	void getChoiceById_ReturnChoice_WhenSuccessful() {
		ResponseEntity<ChoiceModel> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.GET,
				getValidAuthentication(), ChoiceModel.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
				() -> assertEquals(choiceToSave, exchange.getBody())
		);
	}
	
	@Test
	@DisplayName("getChoiceById return status code 404 when choice not found")
	void getChoiceById_Return404_WhenChoiceNotFound() {
		BDDMockito.when(choiceServiceMock.getChoiceByIdOrThrowChoiceNotFoundException(anyLong()))
				.thenThrow(ChoiceNotFoundException.class);
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.GET,
				getValidAuthentication(), Object.class);
		
		log.info(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("findByTitle return a list of choice when successful")
	void findByTitle_ReturnChoiceList_WhenSuccessful() {
		ParameterizedTypeReference<List<ChoiceModel>> typeReference = new ParameterizedTypeReference<List<ChoiceModel>>() {
		};
		
		ResponseEntity<List<ChoiceModel>> exchange = testRestTemplate.exchange(url + "/list/1/?title=", HttpMethod.GET,
				getValidAuthentication(), typeReference);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertFalse(exchange.getBody().isEmpty()),
				() -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
				() -> assertEquals(1, exchange.getBody().size()),
				() -> assertTrue(exchange.getBody().contains(choiceToSave))
		);
	}
	
	@Test
	@DisplayName("findByTitle return status code 404 when choice not found")
	void findByTitle_ReturnStatusCode404_WhenChoiceNotFound() {
		BDDMockito.when(choiceServiceMock.findByQuestionAndTitle(anyLong(), anyString()))
		.thenReturn(List.of());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/list/1/?title=", HttpMethod.GET,
				getValidAuthentication(), Object.class);
		
		log.info("TESTE {}", exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("findByTitle return status code 404 when question not found")
	void findByTitle_ReturnStatusCode404_WhenQuestionNotFound() {
		BDDMockito.when(choiceServiceMock.findByQuestionAndTitle(anyLong(), anyString()))
				.thenThrow(QuestionNotFoundException.class);
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/list/1/?title=", HttpMethod.GET,
				getValidAuthentication(), Object.class);
		
		log.info("TESTE {}", exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("save insert choice when successful")
	void save_InsertChoice_WhenSuccessful() {
		HttpEntity<ChoiceDto> httpEntity = new HttpEntity<>(ChoiceCreator.mockChoiceDto(), getValidAuthentication().getHeaders());
		
		ResponseEntity<ChoiceModel> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
				httpEntity, ChoiceModel.class);
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.CREATED, exchange.getStatusCode()),
				() -> assertEquals(choiceToSave, exchange.getBody())
		);
	}
	
	@Test
	@DisplayName("save return status code 400 when choice have invalid fields")
	void save_Return400_WhenChoiceHaveInvalidFields() {
		HttpEntity<ChoiceDto> httpEntity = new HttpEntity<>(ChoiceCreator.mockChoiceDtoInvalid(), getValidAuthentication().getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
				httpEntity, Object.class);
		
		log.info(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("save return status code 404 when question not found")
	void save_Return404_WhenQuestionNotFound() {
		BDDMockito.when(choiceServiceMock.save(any(ChoiceDto.class)))
				.thenThrow(QuestionNotFoundException.class);
		
		HttpEntity<ChoiceDto> httpEntity = new HttpEntity<>(ChoiceCreator.mockChoiceDto(),
				getValidAuthentication().getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
				httpEntity, Object.class);
		
		log.info(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace update choice when successful")
	void replace_UpdatedChoice_WhenSuccessful() {
		HttpEntity<ChoiceDto> httpEntity = new HttpEntity<>(ChoiceCreator.mockChoiceDto(), getValidAuthentication().getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT,
				httpEntity, Object.class);
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace return status code 404 when question not found")
	void replace_Return404_WhenQuestionNotFound() {
		BDDMockito.doThrow(QuestionNotFoundException.class).when(choiceServiceMock).replace(anyLong(), any(ChoiceDto.class));
		
		HttpEntity<ChoiceDto> httpEntity = new HttpEntity<>(ChoiceCreator.mockChoiceDto(),
				getValidAuthentication().getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT,
				httpEntity, Object.class);
		
		log.info(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace return status code 404 when choice not found")
	void replace_Return404_WhenChoiceNotFound() {
		BDDMockito.doThrow(ChoiceNotFoundException.class).when(choiceServiceMock).replace(anyLong(), any(ChoiceDto.class));
		
		HttpEntity<ChoiceDto> httpEntity = new HttpEntity<>(ChoiceCreator.mockChoiceDto(),
				getValidAuthentication().getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT,
				httpEntity, Object.class);
		
		log.info(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace return status code 400 when choicedto have invalid fields")
	void replace_Return400_WhenChoiceDtoHaveInvalidFields() {
		HttpEntity<ChoiceDto> httpEntity = new HttpEntity<>(ChoiceCreator.mockChoiceDtoInvalid(),
				getValidAuthentication().getHeaders());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT,
				httpEntity, Object.class);
		
		log.info(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("delete remove choice when successful")
	void delete_RemoveChoice_WhenSuccessful() {
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.DELETE,
				getValidAuthentication(), Object.class);
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("delete return status code 404 when choice not found")
	void delete_Return404_WhenChoiceNotFound() {
		BDDMockito.doThrow(ChoiceNotFoundException.class).when(choiceServiceMock).delete(anyLong());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.DELETE,
				getValidAuthentication(), Object.class);
		
		log.info(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("delete return status code 404 when question not found")
	void delete_Return404_WhenQuestionNotFound() {
		BDDMockito.doThrow(QuestionNotFoundException.class).when(choiceServiceMock).delete(anyLong());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.DELETE,
				getValidAuthentication(), Object.class);
		
		log.info(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange.getBody()),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
				);
	}
	

}
