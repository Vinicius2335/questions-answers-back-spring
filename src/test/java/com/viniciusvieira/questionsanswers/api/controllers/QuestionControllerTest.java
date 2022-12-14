package com.viniciusvieira.questionsanswers.api.controllers;

import com.viniciusvieira.questionsanswers.api.representation.models.QuestionDto;
import com.viniciusvieira.questionsanswers.domain.exception.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.services.CascadeDeleteService;
import com.viniciusvieira.questionsanswers.domain.services.QuestionService;
import com.viniciusvieira.questionsanswers.util.QuestionCreator;
import lombok.extern.log4j.Log4j2;
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
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Test for question controller")
@Log4j2
class QuestionControllerTest {
    @MockBean
    private QuestionService questionServiceMock;
    @MockBean
    private CascadeDeleteService cascadeDeleteServiceMock;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final String url = "/api/professor/course/question";
    private String token;
    private QuestionModel questionMock;
    private List<QuestionModel> questionMockList;

    @BeforeEach
    void setUp() {
        questionMock = QuestionCreator.mockQuestion();
        questionMockList = List.of(questionMock);

        String body = "{\"username\":\"vinicius\",\"password\":\"devdojo\"}";
        token = "Bearer " + testRestTemplate.postForEntity("/api/login", body, String.class).getBody();

        // findByIdOrThrowQuestionNotFoundException
        BDDMockito.when(questionServiceMock.findByIdOrThrowQuestionNotFoundException(anyLong()))
                .thenReturn(questionMock);

        // findByCourseAndTitle
        BDDMockito.when(questionServiceMock.findByCourseAndTitle(anyLong(), anyString()))
                .thenReturn(questionMockList);

        // save
        BDDMockito.when(questionServiceMock.save(any(QuestionDto.class)))
                .thenReturn(questionMock);

        // delete
        //BDDMockito.doNothing().when(questionServiceMock).delete(anyLong());
        BDDMockito.doNothing().when(cascadeDeleteServiceMock).deleteQuestionAndAllRelatedEntities(anyLong());

        // replace
        BDDMockito.doNothing().when(questionServiceMock).replace(anyLong(), any(QuestionDto.class));

    }

    public HttpEntity<Void> getValidAuthentication() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, this.token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<Void>(headers);
    }

    @Test
    @DisplayName("findByIdOrThrowQuestionNotFoundException return a question when successful")
    void findByIdOrThrowQuestionNotFoundException_ReturnQuestion_WhenSuccessful() {
        ResponseEntity<QuestionModel> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.GET,
                getValidAuthentication(), QuestionModel.class);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.OK, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("findByIdOrThrowQuestionNotFoundException Return status code 404 when question not founded by id")
    void findByIdOrThrowQuestionNotFoundException_Return404_WhenQuestionNotFoundById() {
        BDDMockito.when(questionServiceMock.findByIdOrThrowQuestionNotFoundException(anyLong()))
                .thenThrow(QuestionNotFoundException.class);

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.GET,
                getValidAuthentication(), Object.class);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("findByCourseAndTitle Return a list of question when successful")
    void findByCourseAndTitle_ReturnListQuestion_WhenSuccessfu() {
        ParameterizedTypeReference<List<QuestionModel>> typeReference = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<QuestionModel>> exchange = testRestTemplate.exchange(url + "/list/1/?title=",
                HttpMethod.GET, getValidAuthentication(), typeReference);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
                () -> assertEquals(questionMockList, exchange.getBody()),
                () -> assertTrue(exchange.getBody().contains(questionMock))
        );
    }

    @Test
    @DisplayName("findByCourseAndTitle Return status code 404 when no question is found ")
    void findByCourseAndTitle_Return404_WhenNoQuestionIsFound() {
        BDDMockito.when(questionServiceMock.findByCourseAndTitle(anyLong(), anyString()))
                .thenReturn(List.of());

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/list/1/?title=",
                HttpMethod.GET, getValidAuthentication(), Object.class);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("save insert question when successful")
    void save_InsertQuestion_WhenSuccessful() {
        HttpEntity<QuestionDto> httpEntity = new HttpEntity<>(QuestionCreator.mockQuestionDto(),
                getValidAuthentication().getHeaders());

        ResponseEntity<QuestionModel> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
                httpEntity, QuestionModel.class);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.CREATED, exchange.getStatusCode()),
                () -> assertEquals(questionMock, exchange.getBody())
        );
    }

    @Test
    @DisplayName("save Return status code 400 when questionDto have invalid field")
    void save_Return400_WhenQuestionDtoHaveInvalidField() {
        HttpEntity<QuestionDto> httpEntity = new HttpEntity<>(QuestionCreator.mockQuestionDtoInvalid(),
                getValidAuthentication().getHeaders());

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "?idCourse=1", HttpMethod.POST,
                httpEntity, Object.class);

        log.info(exchange.getBody());

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("save Return status code 404 when Course not found by id")
    void save_Return404_WhenCourseNotFoundById() {
        BDDMockito.when(questionServiceMock.save(any(QuestionDto.class)))
                .thenThrow(CourseNotFoundException.class);

        HttpEntity<QuestionDto> httpEntity = new HttpEntity<>(QuestionCreator.mockQuestionDto(),
                getValidAuthentication().getHeaders());

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "?idCourse=1", HttpMethod.POST,
                httpEntity, Object.class);

        log.info(exchange.getBody());

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("delete remove question when successful")
    void delete_RemoveQuestion_whenSuccessful() {
        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.DELETE,
                getValidAuthentication(), Object.class);

        assertAll(
                () -> assertNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
        );

    }

    @Test
    @DisplayName("delete return status code 404 when question not found by id")
    void delete_Return404_whenQuestionNotFoundById() {
        //BDDMockito.doThrow(QuestionNotFoundException.class).when(questionServiceMock).delete(anyLong());
        BDDMockito.doThrow(QuestionNotFoundException.class)
                .when(cascadeDeleteServiceMock).deleteQuestionAndAllRelatedEntities(anyLong());

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.DELETE,
                getValidAuthentication(), Object.class);

        log.info(exchange.getBody());

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("update replace question when successful")
    void update_ReplaceQuestion_WhenSuccessful() {
        HttpEntity<QuestionDto> httpEntity = new HttpEntity<>(QuestionCreator.mockQuestionDto(),
                getValidAuthentication().getHeaders());

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT, httpEntity,
                Object.class);

        assertAll(
                () -> assertNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("update return status code 400 when questionDto have invalid field")
    void update_Return400_WhenQuestionDtoHaveInvalidField() {

        HttpEntity<QuestionDto> httpEntity = new HttpEntity<>(QuestionCreator.mockQuestionDtoInvalid(),
                getValidAuthentication().getHeaders());

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT, httpEntity,
                Object.class);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("update return status code 404 when question not found by id")
    void update_Return404_WhenQuestionNotFoundById() {
        BDDMockito.doThrow(QuestionNotFoundException.class).when(questionServiceMock).replace(anyLong(),
                any(QuestionDto.class));

        HttpEntity<QuestionDto> httpEntity = new HttpEntity<>(QuestionCreator.mockQuestionDto(),
                getValidAuthentication().getHeaders());

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT, httpEntity,
                Object.class);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
        );
    }

}
