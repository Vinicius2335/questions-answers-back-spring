package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.api.mappers.v1.CourseMapper;
import com.viniciusvieira.questionsanswers.api.representation.models.CourseDto;
import com.viniciusvieira.questionsanswers.domain.exception.CourseNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final ExtractEntityFromTokenService extractEntityFromTokenService;

    public CourseModel findByIdOrThrowCourseNotFoundException(Long id) {
        Long idProfessor = getIdProfessorFromToken();

        return courseRepository.findOneCourse(id, idProfessor)
                .orElseThrow(() -> new CourseNotFoundException("Course Not Found"));
    }

    private Long getIdProfessorFromToken() {
        return extractEntityFromTokenService.extractProfessorFromToken().getIdProfessor();
    }

    public List<CourseModel> findByName(String name) {
        Long idProfessor = getIdProfessorFromToken();
        return courseRepository.listCoursesByName(name, idProfessor);
    }

    @Transactional
    public CourseModel save(CourseDto courseDto) {
        ProfessorModel professor = extractEntityFromTokenService.extractProfessorFromToken();
        CourseModel course = CourseMapper.INSTANCE.toCorseModel(courseDto);
        course.setProfessor(professor);
        course.setEnabled(true);
        return courseRepository.save(course);
    }

    @Transactional
    public void replace(Long id, CourseDto courseDto) {
        CourseModel course = CourseMapper.INSTANCE.toCorseModel(courseDto);
        CourseModel courseFound = findByIdOrThrowCourseNotFoundException(id);

        courseFound.setName(course.getName());
        courseRepository.save(courseFound);
    }

    @Transactional
    public void delete(Long id) {
        CourseModel course = findByIdOrThrowCourseNotFoundException(id);
        Long idProfessor = getIdProfessorFromToken();

        courseRepository.deleteById(course.getIdCourse(), idProfessor);
    }

}
