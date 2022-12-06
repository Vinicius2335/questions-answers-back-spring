package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.api.mappers.v1.QuestionMapper;
import com.viniciusvieira.questionsanswers.api.representation.models.QuestionDto;
import com.viniciusvieira.questionsanswers.domain.exception.QuestionNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import com.viniciusvieira.questionsanswers.domain.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final CourseService courseService;
    private final ExtractEntityFromTokenService extractEntityFromTokenService;

    public QuestionModel findByIdOrThrowQuestionNotFoundException(Long id) {
        Long idProfessor = getIdProfessorFromToken();

        return questionRepository.findOneQuestion(id, idProfessor)
                .orElseThrow(() -> new QuestionNotFoundException("Question Not Found"));
    }

    private Long getIdProfessorFromToken() {
        return extractEntityFromTokenService.extractProfessorFromToken().getIdProfessor();
    }

    public List<QuestionModel> findByCourseAndTitle(Long idCourse, String title) {
        Long idProfessor = getIdProfessorFromToken();
        return questionRepository.listQuestionByCourseAndTitle(idCourse, title, idProfessor);
    }

    @Transactional
    public QuestionModel save(QuestionDto questionDto) {
        QuestionModel question = QuestionMapper.INSTANCE.toQuestionModel(questionDto);
        CourseModel course = courseService.findByIdOrThrowCourseNotFoundException(question.getCourse().getIdCourse());

        question.setProfessor(course.getProfessor());
        question.setEnabled(true);
        return questionRepository.save(question);
    }

    @Transactional
    public void replace(Long id, QuestionDto questionDto) {
        QuestionModel question = QuestionMapper.INSTANCE.toQuestionModel(questionDto);
        QuestionModel questionFound = findByIdOrThrowQuestionNotFoundException(id);

        questionFound.setTitle(question.getTitle());
        questionRepository.save(questionFound);
    }

    @Transactional
    public void delete(Long id) {
        QuestionModel question = findByIdOrThrowQuestionNotFoundException(id);
        Long idProfessor = getIdProfessorFromToken();

        questionRepository.deleteById(question.getIdQuestion(), idProfessor);
    }

    @Transactional
    public void deleteAllQuestionsRelatedToCouse(Long idCourse) {
        Long idProfessor = getIdProfessorFromToken();
        questionRepository.deleteAllQuestionsRelatedToCouse(idCourse, idProfessor);
    }

    public List<QuestionModel> listQuestionsByCourseNotAssociatedWithAnAssignment(Long courseId,
                                                                                  Long assignmentId) {
        Long idProfessor = getIdProfessorFromToken();
        return questionRepository.findAllQuestionsByCourseNotAssociatedWithAnAssignment(courseId,
                assignmentId, idProfessor);
    }

}
