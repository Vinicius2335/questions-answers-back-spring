package com.viniciusvieira.questionsanswers.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viniciusvieira.questionsanswers.api.mappers.QuestionAssignmentMapper;
import com.viniciusvieira.questionsanswers.api.representation.models.QuestionAssignmentDto;
import com.viniciusvieira.questionsanswers.domain.exception.ConflictException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionAssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionAssignmetAlreadyExistsException;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionAssignmentModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.repositories.QuestionAssignmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionAssignmentService {
	private final QuestionAssignmentRepository questionAssignmentRepository;
	private final QuestionService questionService;
	private final AssignmentService assignmentService;
	private final ExtractProfessorService extractProfessorService;
	
	public QuestionAssignmentModel findQuestionAssignmentOrThrowsQuestionAssignmentNotFound(Long idQuestionAssignment) {
		ProfessorModel professor = extractProfessorService.extractProfessorFromToken();
		
		return questionAssignmentRepository.findOneQuestionAssignment(idQuestionAssignment, 
				professor.getIdProfessor())
				.orElseThrow(() -> new QuestionAssignmentNotFoundException("Question Not Found!"));
	}
	
	public List<QuestionModel> findQuestionById(Long courseId, Long assignmentId) {
		List<QuestionModel> questionsList = listValidQuestionsByCourseNotAssociatedWithAnAssignment(courseId,
				assignmentId);
		
		List<QuestionModel> validQuestions = questionsList.stream()
				.filter(question -> hasMoreThanOneChoices(question) 
				&& hasOnlyCorrectAnswer(question)).toList();
		
		if (validQuestions.isEmpty()) {
			throw new QuestionNotFoundException("Question Not Found!");
		}
		
		return validQuestions;
	}

	private List<QuestionModel> listValidQuestionsByCourseNotAssociatedWithAnAssignment(Long courseId,
			Long assignmentId) {
		return questionService.listQuestionsByCourseNotAssociatedWithAnAssignment(courseId, assignmentId);
	}
	
	private boolean hasOnlyCorrectAnswer(QuestionModel question) {
		return question.getChoices().stream().filter(ChoiceModel::isCorrectAnswer).count() == 1;
	}

	private boolean hasMoreThanOneChoices(QuestionModel question) {
		return question.getChoices().size() > 1;
	}
	
	public List<QuestionAssignmentModel> listByAssignment(Long assignmentId){
		assignmentService.findAssignmentOrThrowsAssignmentNotFoundException(assignmentId);
		ProfessorModel professor = extractProfessorService.extractProfessorFromToken();
		return questionAssignmentRepository.listQuestionAssignmentByAssignmentId(assignmentId,
				professor.getIdProfessor());
	}
	
	@Transactional
	public QuestionAssignmentModel save(QuestionAssignmentDto questionAssignmentDto) {
		questionService.findByIdOrThrowQuestionNotFoundException(questionAssignmentDto.getQuestion().getIdQuestion());
		assignmentService.findAssignmentOrThrowsAssignmentNotFoundException(questionAssignmentDto.getAssignment().getIdAssignment());
		
		QuestionAssignmentModel questionAssignmentToSave = QuestionAssignmentMapper.INSTANCE.toQuestionAssignmentModel(questionAssignmentDto);
		questionAssignmentToSave.setEnabled(true);
		questionAssignmentToSave.setProfessor(extractProfessorService.extractProfessorFromToken());
		
		if (isQuestionAlreadyAssociatedWithAssignment(questionAssignmentToSave)) {
			throw new QuestionAssignmetAlreadyExistsException("Question Assignment Association Already Exists");
		}
		
		return questionAssignmentRepository.save(questionAssignmentToSave);
	}
	
	private boolean isQuestionAlreadyAssociatedWithAssignment(QuestionAssignmentModel questionAssignment) {
		List<QuestionAssignmentModel> questionAssignments = questionAssignmentRepository.listQuestionAssignmentByQuestionAndAssignment(
				questionAssignment.getQuestion().getIdQuestion(),
				questionAssignment.getAssignment().getIdAssignment(),
				questionAssignment.getProfessor().getIdProfessor());
		
		return !questionAssignments.isEmpty();
	}
	
	@Transactional
	public void replace(Long idQuestionAssignment, @Valid QuestionAssignmentDto questionAssignmentDto) {
		QuestionAssignmentModel questionAssignmentToUpdate = findQuestionAssignmentOrThrowsQuestionAssignmentNotFound(idQuestionAssignment);
		
		questionAssignmentToUpdate.setGrade(questionAssignmentDto.getGrade());
		questionAssignmentToUpdate.setQuestion(questionAssignmentDto.getQuestion());
		questionAssignmentToUpdate.setAssignment(questionAssignmentDto.getAssignment());
		
		questionAssignmentRepository.save(questionAssignmentToUpdate);
	}
	
	@Transactional
	public void delete(Long idQuestionAssignment) {
		findQuestionAssignmentOrThrowsQuestionAssignmentNotFound(idQuestionAssignment);
		ProfessorModel professor = extractProfessorService.extractProfessorFromToken();
		questionAssignmentRepository.deleteById(idQuestionAssignment, professor.getIdProfessor());
	}
	
	public void throwsConflictExceptionIfQuestionIsBeingUseInAnyAssignment(
			long questionId) {
		Long idProfessor = extractProfessorService.extractProfessorFromToken().getIdProfessor();
		List<QuestionAssignmentModel> questionAssignments = questionAssignmentRepository
				.listQuestionAssignmentByQuestionId(questionId, idProfessor);
		
		if(questionAssignments.isEmpty()) {
			return;
		} else {
			String assignments = questionAssignments.stream()
					.map(qa -> qa.getAssignment().getTitle())
					.collect(Collectors.joining(", "));
			throw new ConflictException("This choice cannot be deleted because this questions is being used in"
					+ " the folling assignment: " + assignments);
		}
	}
	
	public void deleteAllQuestionAssignmentRelatedToCourse(Long courseId) {
		ProfessorModel professor = extractProfessorService.extractProfessorFromToken();
		questionAssignmentRepository.deleteAllQuestionAssignmentRelatedToCourse(courseId,
				professor.getIdProfessor());
	}
	
	public void deleteAllQuestionAssignmentRelatedToAssignment(Long assignmentId) {
		ProfessorModel professor = extractProfessorService.extractProfessorFromToken();
		questionAssignmentRepository.deleteAllQuestionAssignmentRelatedToAssignment(assignmentId,
				professor.getIdProfessor());
	}
	
	public void deleteAllQuestionAssignmentRelatedToQuestion(Long questionId) {
		ProfessorModel professor = extractProfessorService.extractProfessorFromToken();
		questionAssignmentRepository.deleteAllQuestionAssignmentRelatedToQuestion(questionId,
				professor.getIdProfessor());
	}

}
