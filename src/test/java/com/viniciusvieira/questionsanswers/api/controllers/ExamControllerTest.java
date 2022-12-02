package com.viniciusvieira.questionsanswers.api.controllers;

import com.viniciusvieira.questionsanswers.domain.exception.ChoiceNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.services.ExamService;
import com.viniciusvieira.questionsanswers.util.ChoiceCreator;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Test for exam controller")
class ExamControllerTest {
    @MockBean
    private ExamService examServiceMock;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String token;
    private final String url = "/api/student/exam";

    @BeforeEach
    void setUp() {

        String body = "{\"username\":\"estudante\",\"password\":\"estudante\"}";
        token = "Bearer " + testRestTemplate.postForEntity("/api/login", body, String.class).getBody();

        BDDMockito.when(examServiceMock.findAllQuestionsByAccessCode(any()))
                .thenReturn(List.of(QuestionCreator.mockQuestion()));

        BDDMockito.when(examServiceMock.findAllChoicesByAccessCode(any()))
                .thenReturn(List.of(ChoiceCreator.mockChoice()));
    }

    public HttpEntity<Void> getValidAuthentication(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, this.token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    @Test
    @DisplayName("findAllQuestionsByAccessCode return a questionList when successful")
    void findAllQuestionsByAccessCode_ReturnQuestionList_WhenSuccessful() {
        List<QuestionModel> expectedQuestions = List.of(QuestionCreator.mockQuestion());
        ParameterizedTypeReference<List<QuestionModel>> typeReference = new ParameterizedTypeReference<>(){};

        ResponseEntity<List<QuestionModel>> exchange = testRestTemplate.exchange(url + "/questions/1",
                HttpMethod.GET, getValidAuthentication(), typeReference);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
                () -> assertEquals(expectedQuestions, exchange.getBody())
        );
    }

    @Test
    @DisplayName("findAllQuestionsByAccessCode return 404 when question not found")
    void findAllQuestionsByAccessCode_Return404_WhenQuestionNotFound() {
        BDDMockito.when(examServiceMock.findAllQuestionsByAccessCode(any()))
                .thenThrow(QuestionNotFoundException.class);

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/questions/99",
                HttpMethod.GET, getValidAuthentication(), Object.class);

        log.info(exchange.getBody());

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("findAllChoicesByAccessCode return choiceList when successful")
    void findAllChoicesByAccessCode_ReturnChoiceList_WhenSuccessful() {
        List<ChoiceModel> expectedChoices = List.of(ChoiceCreator.mockChoice());
        ParameterizedTypeReference<List<ChoiceModel>> typeReference = new ParameterizedTypeReference<>(){};

        ResponseEntity<List<ChoiceModel>> exchange = testRestTemplate.exchange(url + "/choices/1", HttpMethod.GET,
                getValidAuthentication(), typeReference);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
                () -> assertEquals(expectedChoices, exchange.getBody())
        );
    }

    @Test
    @DisplayName("findAllChoicesByAccessCode return 404 when choice not found")
    void findAllChoicesByAccessCode_Return404_WhenChoiceNotFound() {
        BDDMockito.when(examServiceMock.findAllChoicesByAccessCode(any()))
                .thenThrow(ChoiceNotFoundException.class);

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/choices/99", HttpMethod.GET,
                getValidAuthentication(), Object.class);

        log.info(exchange.getBody());

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
        );
    }
}