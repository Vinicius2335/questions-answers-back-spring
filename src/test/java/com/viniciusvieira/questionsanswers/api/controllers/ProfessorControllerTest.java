package com.viniciusvieira.questionsanswers.api.controllers;

import com.viniciusvieira.questionsanswers.domain.exception.ProfessorNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.services.ProfessorService;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Test for professor controller")
class ProfessorControllerTest {
    @MockBean
    private ProfessorService professorServiceMock;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private ProfessorModel expectedProfessor;
    private String token;
    private final String url = "/api/professor";

    @BeforeEach
    void setUp() {
        expectedProfessor = ProfessorCreator.mockProfessor();

        String body = "{\"username\":\"vinicius\",\"password\":\"devdojo\"}";
        token = "Bearer " + testRestTemplate.postForEntity("/api/login", body, String.class).getBody();

        BDDMockito.when(professorServiceMock.findByIdOrThrowProfessorNotFoundException(anyLong()))
                .thenReturn(expectedProfessor);
    }

    public HttpEntity<Void> getValidAuthentication() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, this.token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    @Test
    @DisplayName("getProfessorById return a professor when successful")
    void getProfessorById_ReturnProfessor_WhenSuccessful() {
        ResponseEntity<ProfessorModel> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.GET,
                getValidAuthentication(), ProfessorModel.class);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(expectedProfessor, exchange.getBody()),
                () -> assertEquals(HttpStatus.OK, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("getProfessorById return 404 when professor not found")
    void getProfessorById_Return404_WhenProfessorNotFound() {
        BDDMockito.when(professorServiceMock.findByIdOrThrowProfessorNotFoundException(anyLong()))
                .thenThrow(ProfessorNotFoundException.class);

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.GET,
                getValidAuthentication(), Object.class);

        log.info(exchange.getBody());

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
        );
    }
}