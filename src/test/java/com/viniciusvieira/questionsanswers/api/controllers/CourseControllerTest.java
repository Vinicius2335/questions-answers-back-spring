package com.viniciusvieira.questionsanswers.api.controllers;

import com.viniciusvieira.questionsanswers.api.representation.models.CourseDto;
import com.viniciusvieira.questionsanswers.domain.exception.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;
import com.viniciusvieira.questionsanswers.domain.services.CascadeDeleteService;
import com.viniciusvieira.questionsanswers.domain.services.CourseService;
import com.viniciusvieira.questionsanswers.util.CourseCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("Test for Course Controller")
class CourseControllerTest {
	@MockBean
	private CourseService courseServiceMock;
	@MockBean
	private CascadeDeleteService cascadeDeleteServiceMock;
	
	@Autowired
	protected MockMvc mockMvc;
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private CourseModel course;
	private CourseModel courseToSave;
	private List<CourseModel> courseList;
	private String token;
	
	@BeforeEach
	void setUp() {
		this.course = CourseCreator.mockCourse();
		this.courseToSave = CourseCreator.mockCourseToSave();
		this.courseList = List.of(course);
		
		String body = "{\"username\":\"vinicius\",\"password\":\"devdojo\"}";
		token = "Bearer " + testRestTemplate.postForEntity("/api/login", body, String.class).getBody();
		
		// getCourseById
		BDDMockito.when(courseServiceMock.findByIdOrThrowCourseNotFoundException(ArgumentMatchers.anyLong()))
				.thenReturn(course);
		
		// findByName
		BDDMockito.when(courseServiceMock.findByName(ArgumentMatchers.anyString()))
				.thenReturn(courseList);
		
		// save
		BDDMockito.when(courseServiceMock.save(ArgumentMatchers.any(CourseDto.class)))
				.thenReturn(courseToSave);
		
		// delete
		//BDDMockito.doNothing().when(courseServiceMock).delete(ArgumentMatchers.anyLong());
		BDDMockito.doNothing().when(cascadeDeleteServiceMock).deleteCourseAndAllRelatedEntities(anyLong());
		
		// replace
		BDDMockito.doNothing().when(courseServiceMock)
				.replace(ArgumentMatchers.anyLong(), ArgumentMatchers.any(CourseDto.class));
		
	}
	
	public HttpEntity<Void> getValidAuthentication(){
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, this.token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(headers);
	}
	
	public HttpEntity<Void> getInvalidAuthentication(){
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "11111");
		return new HttpEntity<>(headers);
	}
	
	@Test
	@DisplayName("getCourseById return status code 403 when token is wrong")
	 void getCourseById_Return403_WhenTokenIsWrongShould() {
		ResponseEntity<String> exchange = testRestTemplate.exchange("/api/professor/course/1",
				HttpMethod.GET, getInvalidAuthentication(), String.class);
		
		assertEquals(HttpStatus.FORBIDDEN, exchange.getStatusCode());
	}
	
	@Test
	@DisplayName("getCourseById return course when successful")
	 void getCourseById_ReturnCourse_WhenSuccessful() throws Exception {
		mockMvc.perform(get("/api/professor/course/1")
				.header(HttpHeaders.AUTHORIZATION, this.token)).andExpectAll(
						status().isOk(),
						content().contentType(MediaType.APPLICATION_JSON),
						jsonPath("$.idCourse").value(course.getIdCourse()),
						jsonPath("$.name").value(course.getName()),
						jsonPath("$.professor.idProfessor").value(course.getProfessor().getIdProfessor())
		);
	}
	
	@Test
	@DisplayName("getCourseById return course when successful (usando rest template)")
	 void getCourseById_ReturnCourse_WhenSuccessful2() {
		ResponseEntity<CourseModel> exchange = testRestTemplate.exchange("/api/professor/course/1",
		HttpMethod.GET, getValidAuthentication(), CourseModel.class);
	
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.OK, exchange.getStatusCode()),
				() -> assertEquals(this.course, exchange.getBody())
		);
	}
	
	@Test
	@DisplayName("getCourseById return status code 404 when course not found")
	 void getCourseById_Return404_WhenCourseNotFound() {
		BDDMockito.when(courseServiceMock.findByIdOrThrowCourseNotFoundException(ArgumentMatchers.anyLong()))
			.thenThrow(CourseNotFoundException.class);
		
		ResponseEntity<CourseModel> exchange = testRestTemplate.exchange("/api/professor/course/99",
				HttpMethod.GET, getValidAuthentication(), CourseModel.class);
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
		
	}
	
	// as vezes dá problema na conversão para o JSON quando só temos um valor
	@Test
	@DisplayName("findByName return list all course when successful")
	 void findByName_ReturnCourseList_WhenSuccessful() {
		ParameterizedTypeReference<List<CourseModel>> typeReference =
				new ParameterizedTypeReference<>() {
		};
		
		ResponseEntity<List<CourseModel>> exchange = testRestTemplate
				.exchange("/api/professor/course/list?name=", HttpMethod.GET,
				getValidAuthentication(), typeReference);
		
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(courseList, exchange.getBody()),
				() -> assertEquals(HttpStatus.OK, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("findByName return status code 404 when course not found")
	 void findByName_Return404_WhenCourseNotFound() {
		BDDMockito.when(courseServiceMock.findByName(ArgumentMatchers.anyString()))
				.thenReturn(List.of());
		
		ResponseEntity<CourseNotFoundException> exchange = testRestTemplate
				.exchange("/api/professor/course/list?name=xaxa", HttpMethod.GET,
				getValidAuthentication(), CourseNotFoundException.class);
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("save insert course when successful")
	 void save_InsertCourse_WhenSuccessful() {
		HttpEntity<CourseModel> httpEntity = new HttpEntity<>(courseToSave, getValidAuthentication()
				.getHeaders());
		
		ResponseEntity<CourseModel> exchange = testRestTemplate.exchange("/api/professor/course",
				HttpMethod.POST, httpEntity, CourseModel.class);
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(courseToSave, exchange.getBody()),
				() -> assertEquals(HttpStatus.CREATED, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("save returne 400 when course name is empty")
	 void save_Return400_WhenCourseNameIsEmpty() {
		CourseDto courseDto = CourseCreator.mockInvalidCourseDto();
		HttpEntity<CourseDto> httpEntity = new HttpEntity<>(courseDto, getValidAuthentication().getHeaders());
		
		ResponseEntity<CourseModel> exchange = testRestTemplate.exchange("/api/professor/course",
				HttpMethod.POST, httpEntity, CourseModel.class);
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("delete remove course when successful")
	 void delete_RemoveCourse_WhenSuccessful() {
		ResponseEntity<Void> exchange = testRestTemplate.exchange("/api/professor/course/1",
				HttpMethod.DELETE, getValidAuthentication(), Void.class);
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("delete return status code 404 when course not found")
	 void delete_Return404_WhenCourseNotFound() {
		
		BDDMockito.doThrow(CourseNotFoundException.class)
				.when(cascadeDeleteServiceMock).deleteCourseAndAllRelatedEntities(anyLong());
		
		ResponseEntity<Object> exchange = testRestTemplate.exchange("/api/professor/course/99", HttpMethod.DELETE,
				getValidAuthentication(), Object.class);
		
		System.out.println();
		System.out.println(exchange.getBody());
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace updated course when successful")
	 void replace_UpdateCourse_WhenSuccessful() {
		CourseDto courseDto = CourseCreator.mockCourseDto();
		HttpEntity<CourseDto> httpEntity = new HttpEntity<>(courseDto, getValidAuthentication().getHeaders());
		
		ResponseEntity<Void> exchange = testRestTemplate.exchange("/api/professor/course/1", HttpMethod.PUT,
				httpEntity, Void.class);
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace return status code 404 when course not found")
	 void replace_Return404_WhenCourseNotFound() {
		BDDMockito.doThrow(CourseNotFoundException.class).when(courseServiceMock)
				.replace(ArgumentMatchers.anyLong(), ArgumentMatchers.any(CourseDto.class));
		
		CourseDto courseDto = CourseCreator.mockCourseDto();
		HttpEntity<CourseDto> httpEntity = new HttpEntity<>(courseDto, getValidAuthentication().getHeaders());
		
		ResponseEntity<Void> exchange = testRestTemplate.exchange("/api/professor/course/99", HttpMethod.PUT,
				httpEntity, Void.class);
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.NOT_FOUND, exchange.getStatusCode())
		);
	}
	
	@Test
	@DisplayName("replace return status code 400 when course name is null or empty")
	 void replace_Return400_WhenCourseNameIsNullOrEmpty() {
		CourseDto courseDto = CourseCreator.mockInvalidCourseDto();
		HttpEntity<CourseDto> httpEntity = new HttpEntity<>(courseDto, getValidAuthentication().getHeaders());
		
		ResponseEntity<Void> exchange = testRestTemplate.exchange("/api/professor/course/1", HttpMethod.PUT,
				httpEntity, Void.class);
		
		assertAll(
				() -> assertNotNull(exchange),
				() -> assertEquals(HttpStatus.BAD_REQUEST, exchange.getStatusCode())
				);
	}
	
}

