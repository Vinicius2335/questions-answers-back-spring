package com.viniciusvieira.questionsanswers.domain.services;

import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CascadeDeleteService {
	private final QuestionService questionService;
	private final ChoiceService choiceService;
	private final CourseService courseService;
	
	public void cascadeDeleteCourseQuestionAndChoice(long idCourse) {
		courseService.delete(idCourse);
		questionService.deleteAllQuestionsRelatedToCouse(idCourse);
		choiceService.deleteAllChoicesRelatedToCourse(idCourse);
	}
	
	public void cascadeDeleteQuestionAndChoice(long idQuestion) {
		questionService.delete(idQuestion);
		choiceService.deleteAllChoicesRelatedToQuestion(idQuestion);
	}
}
