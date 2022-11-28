package com.viniciusvieira.questionsanswers.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.viniciusvieira.questionsanswers.domain.exception.AssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.ChoiceNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.ConflictException;
import com.viniciusvieira.questionsanswers.domain.exception.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.ProfessorNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionAssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionAssignmetAlreadyExistsException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionNotFoundException;

@ControllerAdvice
public class ApiExceptionHandler {
	
	@ExceptionHandler(AssignmentNotFoundException.class)
	public ResponseEntity<ExceptionDetails> handlerAssignmentNotFoundException(
			AssignmentNotFoundException ex){
		return new ResponseEntity<>(
				ExceptionDetails.builder()
				.title("Assignment Not Found Exception, Check the Documentation")
				.status(HttpStatus.NOT_FOUND.value())
				.timestamp(OffsetDateTime.now()).build(), HttpStatus.NOT_FOUND
		);
	}
	
	@ExceptionHandler(ChoiceNotFoundException.class)
	public ResponseEntity<ExceptionDetails> handlerChoiceNotFoundException(
			ChoiceNotFoundException ex){
		return new ResponseEntity<>(
				ExceptionDetails.builder()
				.title("Choice Not Found Exception, Check the Documentation")
				.status(HttpStatus.NOT_FOUND.value())
				.timestamp(OffsetDateTime.now()).build(), HttpStatus.NOT_FOUND
		);
	}
	
	@ExceptionHandler(CourseNotFoundException.class)
	public ResponseEntity<ExceptionDetails> handlerCourseNotFoundException(
			CourseNotFoundException ex){
		return new ResponseEntity<>(
				ExceptionDetails.builder()
				.title("Course Not Found Exception, Check the Documentation")
				.status(HttpStatus.NOT_FOUND.value())
				.timestamp(OffsetDateTime.now()).build(), HttpStatus.NOT_FOUND
		);
	}
	
	@ExceptionHandler(ProfessorNotFoundException.class)
	public ResponseEntity<ExceptionDetails> handlerProfessorNotFoundException(
			ProfessorNotFoundException ex){
		return new ResponseEntity<>(
				ExceptionDetails.builder()
				.title("Professor Not Found Exception, Check the Documentation")
				.status(HttpStatus.NOT_FOUND.value())
				.timestamp(OffsetDateTime.now()).build(), HttpStatus.NOT_FOUND
		);
	}
	
	@ExceptionHandler(QuestionNotFoundException.class)
	public ResponseEntity<ExceptionDetails> handlerQuestionNotFoundException(
			QuestionNotFoundException ex){
		return new ResponseEntity<>(
				ExceptionDetails.builder()
				.title("Question Not Found Exception, Check the Documentation")
				.status(HttpStatus.NOT_FOUND.value())
				.timestamp(OffsetDateTime.now()).build(), HttpStatus.NOT_FOUND
		);
	}
	
	@ExceptionHandler(QuestionAssignmentNotFoundException.class)
	public ResponseEntity<ExceptionDetails> handlerQuestionAssignmentNotFoundException(
			QuestionAssignmentNotFoundException ex){
		return new ResponseEntity<>(
				ExceptionDetails.builder()
				.title("Question Assignment Not Found Exception, Check the Documentation")
				.status(HttpStatus.NOT_FOUND.value())
				.timestamp(OffsetDateTime.now()).build(), HttpStatus.NOT_FOUND
		);
	}
	
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ExceptionDetails> handlerConflictException(
			ConflictException ex){
		return new ResponseEntity<>(
				ExceptionDetails.builder()
				.title("Conflict Exception, Check the Documentation")
				.status(HttpStatus.CONFLICT.value())
				.timestamp(OffsetDateTime.now()).build(), HttpStatus.CONFLICT
		);
	}
	
	@ExceptionHandler(QuestionAssignmetAlreadyExistsException.class)
	public ResponseEntity<ExceptionDetails> handlerQuestionAssignmentAlreadyExistsException(
			QuestionAssignmetAlreadyExistsException ex){
		return new ResponseEntity<>(
				ExceptionDetails.builder()
				.title("Question Assignment Already Exists, Check the Documentation")
				.status(HttpStatus.NOT_MODIFIED.value())
				.timestamp(OffsetDateTime.now()).build(), HttpStatus.NOT_MODIFIED
		);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionDetails> handlerMethodArgumentNotValidException(
			MethodArgumentNotValidException ex){
		List<ExceptionDetails.Field> fields = new ArrayList<>();
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		
		fieldErrors.forEach(fieldError -> fields.add(new ExceptionDetails.Field(
				fieldError.getField(),
				fieldError.getDefaultMessage()
			))
		);
		
		return new ResponseEntity<>(
				ExceptionDetails.builder()
				.title("Bad Request Exception, Check the Documentation")
				.status(HttpStatus.BAD_REQUEST.value())
				.fields(fields)
				.timestamp(OffsetDateTime.now()).build(), HttpStatus.BAD_REQUEST
		);
	}
}
