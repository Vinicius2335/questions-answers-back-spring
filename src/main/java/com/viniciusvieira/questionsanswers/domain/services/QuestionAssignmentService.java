package com.viniciusvieira.questionsanswers.domain.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.viniciusvieira.questionsanswers.domain.excepiton.QuestionAssignmentNotFoundException;
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
	private final ExtractProfessorService extractProfessorService;
	
	public QuestionAssignmentModel findQuestionAssignmentOrThrowsQuestionAssignmentNotFound(Long idQuestionAssignment) {
		ProfessorModel professor = extractProfessorService.extractProfessorFromToken();
		
		return questionAssignmentRepository.findOneQuestionAssignment(idQuestionAssignment, 
				professor.getIdProfessor())
				.orElseThrow(() -> new QuestionAssignmentNotFoundException("Question Not Found!"));
	}

	public List<QuestionModel> listValidQuestionsByCourseNotAssociatedWithAnAssignment(Long courseId,
			Long assignmentId) {
		return questionService.listQuestionsByCourseNotAssociatedWithAnAssignment(courseId, assignmentId);
	}
	
	
}
