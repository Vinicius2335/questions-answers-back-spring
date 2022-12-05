package com.viniciusvieira.questionsanswers.api.controllers;

import com.viniciusvieira.questionsanswers.api.representation.models.ApplicationUserDto;
import com.viniciusvieira.questionsanswers.api.representation.models.ProfessorDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.ApplicationUserRequestBody;
import com.viniciusvieira.questionsanswers.api.representation.requests.ProfessorRequestBody;
import com.viniciusvieira.questionsanswers.domain.exception.ProfessorNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.services.ProfessorService;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.ProfessorCreator;
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
import static org.mockito.ArgumentMatchers.*;

@Log4j2
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test admin professor controller")
class AdminProfessorControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private ProfessorService professorServiceMock;

    private final String url = "/api/admin/professor";
    private String token;
    private ProfessorRequestBody expectedProfessorRequestBody;
    private ProfessorDto expectedProfessorDto;
    private ApplicationUserRequestBody expectedApplicationUserRequestBody;
    private ApplicationUserDto expectedApplicationUserDto;
    private List<ProfessorDto> expectedProfessorsDto;

    @BeforeEach
    void setUp() {
        String body = "{\"username\":\"admin\",\"password\":\"admin\"}";
        token = "Bearer " + testRestTemplate.postForEntity("/api/login", body, String.class).getBody();

        expectedProfessorRequestBody = ProfessorCreator.mockProfessorRequestBody();
        expectedProfessorDto = ProfessorCreator.mockProfessorDto();
        expectedApplicationUserRequestBody = ApplicationUserCreator.mockApplicationUserRequestBody();
        expectedApplicationUserDto = ApplicationUserCreator.mockApplicationUserDto();
        expectedProfessorsDto = List.of(expectedProfessorDto);

        //saveProfessor
        BDDMockito.when(professorServiceMock.saveProfessor(any(ProfessorRequestBody.class)))
                .thenReturn(expectedProfessorDto);
        //saveApplicationUserProfessor
        BDDMockito.when(professorServiceMock.saveApplicationUserProfessor(anyLong(),
                any(ApplicationUserRequestBody.class))).thenReturn(expectedApplicationUserDto);
        //findByName
        BDDMockito.when(professorServiceMock.findByName(anyString())).thenReturn(expectedProfessorsDto);
        // replace
        BDDMockito.when(professorServiceMock.replace(anyLong(), any(ProfessorRequestBody.class)))
                .thenReturn(expectedProfessorDto);
        //delete
        BDDMockito.doNothing().when(professorServiceMock).delete(anyLong());
    }

    public HttpEntity<Void> getValidAuthentication(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, this.token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    @Test
    @DisplayName("save insert professor when successful")
    void save_InsertProfessor_WhenSuccessful() {
        HttpEntity<ProfessorRequestBody> httpEntity = new HttpEntity<>(expectedProfessorRequestBody,
                getValidAuthentication().getHeaders());

        ResponseEntity<ProfessorDto> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
                httpEntity, ProfessorDto.class);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.CREATED, exchange.getStatusCode()),
                () -> assertEquals(expectedProfessorDto, exchange.getBody())
        );
    }

    @Test
    @DisplayName("save return status code 400 when professor have invalid fields")
    void save_Return400_WhenProfessorHaveInvalidFields() {
        ProfessorRequestBody invalidProfessorRequestBody = ProfessorCreator.mockInvalidRequestBody();
        HttpEntity<ProfessorRequestBody> httpEntity = new HttpEntity<>(invalidProfessorRequestBody,
                getValidAuthentication().getHeaders());

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
                httpEntity, Object.class);

        log.info(exchange.getBody());

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("saveProfessorApplicationUser insert application user professor when successful")
    void saveProfessorApplicationUser_InsertApplicationUserProfessor_WhenSuccessful() {
        HttpEntity<ApplicationUserRequestBody> httpEntity = new HttpEntity<>(expectedApplicationUserRequestBody,
                getValidAuthentication().getHeaders());

        ResponseEntity<ApplicationUserDto> exchange = testRestTemplate.exchange(url + "/1",
                HttpMethod.POST, httpEntity, ApplicationUserDto.class);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.CREATED, exchange.getStatusCode()),
                () -> assertEquals(expectedApplicationUserDto.getUsername(), exchange.getBody().getUsername())
        );
    }

    @Test
    @DisplayName("saveProfessorApplicationUser return status code 400 when application user have invalid fields")
    void saveProfessorApplicationUser_Return400_WhenApplicationUserHaveInvalidFields() {
        ApplicationUserRequestBody invalidApplicationUserRequestBody = ApplicationUserCreator.mockInvalidApplicationUserRequestBody();
        HttpEntity<ApplicationUserRequestBody> httpEntity = new HttpEntity<>(invalidApplicationUserRequestBody,
                getValidAuthentication().getHeaders());

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1",
                HttpMethod.POST, httpEntity, Object.class);

        log.info(exchange.getBody());

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("saveProfessorApplicationUser return status code 404 when professor not found by id")
    void saveProfessorApplicationUser_Return404_WhenProfessorNotFoundById() {
        BDDMockito.when(professorServiceMock.saveApplicationUserProfessor(anyLong(),
                any(ApplicationUserRequestBody.class))).thenThrow(ProfessorNotFoundException.class);
        HttpEntity<ApplicationUserRequestBody> httpEntity = new HttpEntity<>(expectedApplicationUserRequestBody,
                getValidAuthentication().getHeaders());

        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1",
                HttpMethod.POST, httpEntity, Object.class);

        log.info(exchange.getBody());

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("findByName return professor list when successful")
    void findByName_ReturnProfessorList_WhenSuccessful() {
        ParameterizedTypeReference<List<ProfessorDto>> typeReference = new ParameterizedTypeReference<>(){};
        ResponseEntity<List<ProfessorDto>> exchange = testRestTemplate.exchange(url + "/professors", HttpMethod.GET,
                getValidAuthentication(), typeReference);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
                () -> assertEquals(expectedProfessorsDto, exchange.getBody())
        );
    }

    @Test
    @DisplayName("findByName return status code 404 when professor not found by id")
    void findByName_Return404_WhenProfessorNotFoundById() {
        BDDMockito.when(professorServiceMock.findByName(anyString())).thenThrow(ProfessorNotFoundException.class);
        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/professors", HttpMethod.GET,
                getValidAuthentication(), Object.class);

        log.info(exchange.getBody());

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("replece update professor when successful")
    void replace_UpdateProfessor_WhenSuccessful() {
        HttpEntity<ProfessorRequestBody> httpEntity = new HttpEntity<>(expectedProfessorRequestBody,
                getValidAuthentication().getHeaders());
        ResponseEntity<ProfessorDto> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT,
                httpEntity, ProfessorDto.class);

        assertAll(
                () -> assertNotNull(exchange),
                () -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("replece return status code 400 when professor have invalid fields")
    void replace_Return400_WhenProfessorHaveInvalidFields() {
        ProfessorRequestBody invalidProfessorRequestBody = ProfessorCreator.mockInvalidRequestBody();
        HttpEntity<ProfessorRequestBody> httpEntity = new HttpEntity<>(invalidProfessorRequestBody,
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
    @DisplayName("replece return status code 404 when professor not found by id")
    void replace_Return404_WhenProfessorNotFoundById() {
        BDDMockito.when(professorServiceMock.replace(anyLong(), any(ProfessorRequestBody.class)))
                .thenThrow(ProfessorNotFoundException.class);
        HttpEntity<ProfessorRequestBody> httpEntity = new HttpEntity<>(expectedProfessorRequestBody,
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
    @DisplayName("delete remove Professor when successful")
    void delete_RemoveProfessor_WhenSuccessful() {
        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.DELETE,
                getValidAuthentication(), Object.class);

        assertAll(
                () -> assertNotNull(exchange),
                () -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("delete return status code 404 when professor not found by id")
    void delete_Return404_WhenProfessorNotFoundById() {
        BDDMockito.doThrow(ProfessorNotFoundException.class).when(professorServiceMock).delete(anyLong());
        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.DELETE,
                getValidAuthentication(), Object.class);

        log.info(exchange.getBody());

        assertAll(
                () -> assertNotNull(exchange),
                () -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
        );
    }
}