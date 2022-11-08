package com.viniciusvieira.questionsanswers.domain.services;

import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CascadeDeleteService {
	private final QuestionService questionService;
	private final ChoiceService choiceService;
	private final CourseService courseService;
	private final AssignmentService assignmentService;
	private final QuestionAssignmentService questionAssignmentService;
	
	public void deleteCourseAndAllRelatedEntities(long idCourse) {
		courseService.delete(idCourse);
		questionService.deleteAllQuestionsRelatedToCouse(idCourse);
		choiceService.deleteAllChoicesRelatedToCourse(idCourse);
		assignmentService.deleteAllAssignmentRelatedToCourse(idCourse);
		questionAssignmentService.deleteAllQuestionAssignmentRelatedToCourse(idCourse);
	}
	
	public void deleteQuestionAndAllRelatedEntities(long idQuestion) {
		questionService.delete(idQuestion);
		choiceService.deleteAllChoicesRelatedToQuestion(idQuestion);
		questionAssignmentService.deleteAllQuestionAssignmentRelatedToQuestion(idQuestion);
	}
	
	//TEST
	public void deleteAssignmentAndAllRelatedEntities(long idAssignment) {
		assignmentService.delete(idAssignment);
		questionAssignmentService.deleteAllQuestionAssignmentRelatedToAssignment(idAssignment);
	}
	
	//TEST
	public void deleteQuestionAssignmentAndAllRelatedEntities(long idQuestionAssignment) {
		questionAssignmentService.deleteAllQuestionAssignmentRelatedToAssignment(idQuestionAssignment);
	}
	
}
