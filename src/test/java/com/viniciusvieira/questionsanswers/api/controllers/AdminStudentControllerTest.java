package com.viniciusvieira.questionsanswers.api.controllers;

import com.viniciusvieira.questionsanswers.api.representation.models.ApplicationUserDto;
import com.viniciusvieira.questionsanswers.api.representation.models.StudentDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.ApplicationUserRequestBody;
import com.viniciusvieira.questionsanswers.api.representation.requests.StudentRequestBody;
import com.viniciusvieira.questionsanswers.domain.exception.StudentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.services.StudentService;
import com.viniciusvieira.questionsanswers.util.ApplicationUserCreator;
import com.viniciusvieira.questionsanswers.util.StudentCreator;
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
@DisplayName("Test for admin student controller")
class AdminStudentControllerTest {
    @MockBean
    private StudentService studentServiceMock;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final String url = "/api/admin/student";
    private String token;
    private StudentRequestBody expectedStudentRequestBody;
    private StudentDto expectedStudentDto;
    private ApplicationUserRequestBody expectedApplicationUserRequestBody;
    private ApplicationUserDto expectedApplicationUserDto;
    private List<StudentDto> expectedStudentsDto;

    @BeforeEach
    void setUp() {
        String body = "{\"username\":\"admin\",\"password\":\"admin\"}";
        token = "Bearer " + testRestTemplate.postForEntity("/api/login", body, String.class).getBody();

        expectedStudentRequestBody = StudentCreator.mockStudentRequestBody();
        expectedStudentDto = StudentCreator.mockStudentDto();
        expectedStudentsDto = List.of(expectedStudentDto);
        expectedApplicationUserDto = ApplicationUserCreator.mockApplicationUserDto();
        expectedApplicationUserRequestBody = ApplicationUserCreator.mockApplicationUserRequestBody();

        //saveStudent
        BDDMockito.when(studentServiceMock.saveStudent(any(StudentRequestBody.class)))
                .thenReturn(expectedStudentDto);
        //saveApplicationUserStudent
        BDDMockito.when(studentServiceMock.saveApplicationUserStudent(anyLong(), any(ApplicationUserRequestBody.class)))
                .thenReturn(expectedApplicationUserDto);
        //findByName
        BDDMockito.when(studentServiceMock.findByName(anyString())).thenReturn(expectedStudentsDto);
        // replace
        BDDMockito.when(studentServiceMock.replace(anyLong(), any(StudentRequestBody.class)))
                .thenReturn(expectedStudentDto);
        //delete
        BDDMockito.doNothing().when(studentServiceMock).delete(anyLong());
    }

    public HttpEntity<Void> getValidAuthentication() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, this.token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    @Test
    @DisplayName("save insert student when successful")
    void save_InsertStudent_WhenSuccessful() {
        HttpEntity<StudentRequestBody> httpEntity = new HttpEntity<>(expectedStudentRequestBody,
                getValidAuthentication().getHeaders());

        ResponseEntity<StudentDto> exchange = testRestTemplate.exchange(url, HttpMethod.POST,
                httpEntity, StudentDto.class);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.CREATED, exchange.getStatusCode()),
                () -> assertEquals(expectedStudentDto, exchange.getBody())
        );
    }

    @Test
    @DisplayName("save return status code 400 when student have invalid fields")
    void save_Return400_WhenStudentHaveInvalidFields() {
        StudentRequestBody invalidStudentRequestBody = StudentCreator.mockInvalidStudentRequestBody();
        HttpEntity<StudentRequestBody> httpEntity = new HttpEntity<>(invalidStudentRequestBody,
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
    @DisplayName("saveStudentApplicationUser insert application user student when successful")
    void saveStudentApplicationUser_InsertApplicationUserStudent_WhenSuccessful() {
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
    @DisplayName("saveStudentApplicationUser return status code 400 when application user have invalid fields")
    void saveStudentApplicationUser_Return400_WhenApplicationUserHaveInvalidFields() {
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
    @DisplayName("saveStudentApplicationUser return status code 404 when student not found by id")
    void saveStudentApplicationUser_Return404_WhenStudentNotFoundById() {
        BDDMockito.when(studentServiceMock.saveApplicationUserStudent(anyLong(), any(ApplicationUserRequestBody.class)))
                .thenThrow(StudentNotFoundException.class);
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
    @DisplayName("findByName return student list when successful")
    void findByName_ReturnStudentList_WhenSuccessful() {
        ParameterizedTypeReference<List<StudentDto>> typeReference = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<StudentDto>> exchange = testRestTemplate.exchange(url + "/students", HttpMethod.GET,
                getValidAuthentication(), typeReference);

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
                () -> assertEquals(expectedStudentsDto, exchange.getBody())
        );
    }

    @Test
    @DisplayName("findByName return status code 404 when student not found by id")
    void findByName_Return404_WhenStudentNotFoundById() {
        BDDMockito.when(studentServiceMock.findByName(anyString())).thenThrow(StudentNotFoundException.class);
        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/students", HttpMethod.GET,
                getValidAuthentication(), Object.class);

        log.info(exchange.getBody());

        assertAll(
                () -> assertNotNull(exchange.getBody()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("replece update student when successful")
    void replace_UpdateProfessor_WhenSuccessful() {
        HttpEntity<StudentRequestBody> httpEntity = new HttpEntity<>(expectedStudentRequestBody,
                getValidAuthentication().getHeaders());
        ResponseEntity<StudentDto> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.PUT,
                httpEntity, StudentDto.class);

        assertAll(
                () -> assertNotNull(exchange),
                () -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("replece return status code 400 when student have invalid fields")
    void replace_Return400_WhenStudentHaveInvalidFields() {
        StudentRequestBody invalidStudentRequestBody = StudentCreator.mockInvalidStudentRequestBody();
        HttpEntity<StudentRequestBody> httpEntity = new HttpEntity<>(invalidStudentRequestBody,
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
    @DisplayName("replece return status code 404 when student not found by id")
    void replace_Return404_WhenStudentNotFoundById() {
        BDDMockito.when(studentServiceMock.replace(anyLong(), any(StudentRequestBody.class)))
                .thenThrow(StudentNotFoundException.class);
        HttpEntity<StudentRequestBody> httpEntity = new HttpEntity<>(expectedStudentRequestBody,
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
    @DisplayName("delete remove student when successful")
    void delete_RemoveStudent_WhenSuccessful() {
        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.DELETE,
                getValidAuthentication(), Object.class);

        assertAll(
                () -> assertNotNull(exchange),
                () -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
        );
    }

    @Test
    @DisplayName("delete return status code 404 when student not found by id")
    void delete_Return404_WhenStudentNotFoundById() {
        BDDMockito.doThrow(StudentNotFoundException.class).when(studentServiceMock).delete(anyLong());
        ResponseEntity<Object> exchange = testRestTemplate.exchange(url + "/1", HttpMethod.DELETE,
                getValidAuthentication(), Object.class);

        log.info(exchange.getBody());

        assertAll(
                () -> assertNotNull(exchange),
                () -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
        );
    }
}