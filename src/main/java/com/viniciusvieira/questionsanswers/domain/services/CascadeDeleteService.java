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
	
	public void deleteCourseAndAllRelatedEntities(long idCourse) {
		courseService.delete(idCourse);
		questionService.deleteAllQuestionsRelatedToCouse(idCourse);
		choiceService.deleteAllChoicesRelatedToCourse(idCourse);
		assignmentService.deleteAllAssignmentRelatedToCourse(idCourse);
	}
	
	public void deleteQuestionAndAllRelatedEntities(long idQuestion) {
		questionService.delete(idQuestion);
		choiceService.deleteAllChoicesRelatedToQuestion(idQuestion);
	}
	
}
