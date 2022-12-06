package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.api.mappers.v1.AssignmentMapper;
import com.viniciusvieira.questionsanswers.api.representation.models.AssignmentDto;
import com.viniciusvieira.questionsanswers.domain.exception.AssignmentNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;
import com.viniciusvieira.questionsanswers.domain.models.CourseModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import com.viniciusvieira.questionsanswers.domain.repositories.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final CourseService courseService;
    private final ExtractEntityFromTokenService extractEntityFromTokenService;

    public AssignmentModel findAssignmentOrThrowsAssignmentNotFoundException(Long idAssignment) {
        Long idProfessor = getIdProfessorFromToken();
        return assignmentRepository.findOneAssignment(idAssignment, idProfessor)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment Not Found"));
    }

    private Long getIdProfessorFromToken() {
        return extractEntityFromTokenService.extractProfessorFromToken().getIdProfessor();
    }

    public List<AssignmentModel> findByCourseAndTitle(Long idCourse, String title) {
        Long idProfessor = getIdProfessorFromToken();
        CourseModel courseFound = courseService.findByIdOrThrowCourseNotFoundException(idCourse);

        return assignmentRepository.listAssignmentByCourseAndTitle(courseFound.getIdCourse(),
                title, idProfessor);
    }

    @Transactional
    public AssignmentModel save(AssignmentDto assignmentDto) {
        courseService.findByIdOrThrowCourseNotFoundException(assignmentDto.getCourse().getIdCourse());
        AssignmentModel assignmentModel = AssignmentMapper.INSTANCE.toAssignmentModel(assignmentDto);
        ProfessorModel professor = extractEntityFromTokenService.extractProfessorFromToken();

        assignmentModel.setEnabled(true);
        assignmentModel.setProfessor(professor);
        assignmentModel.setAccessCode(generateAccessCode(assignmentModel.getCourse().getIdCourse()));

        return assignmentRepository.save(assignmentModel);
    }

    private String generateAccessCode(Long courseId) {
        Long idProfessor = getIdProfessorFromToken();
        String accessCode = RandomStringUtils.randomAlphanumeric(6);
        while (assignmentRepository.accessCodeExistsForCourse(accessCode, courseId, idProfessor).isPresent()) {
            generateAccessCode(courseId);
        }

        return accessCode;
    }

    @Transactional
    public void replace(Long idAssignment, AssignmentDto assignmentDto) {
        courseService.findByIdOrThrowCourseNotFoundException(assignmentDto.getCourse().getIdCourse());
        AssignmentModel assignmentToUpdate = findAssignmentOrThrowsAssignmentNotFoundException(idAssignment);

        assignmentToUpdate.setTitle(assignmentDto.getTitle());
        assignmentRepository.save(assignmentToUpdate);
    }

    @Transactional
    public void delete(Long idAssignment) {
        findAssignmentOrThrowsAssignmentNotFoundException(idAssignment);

        assignmentRepository.deleteById(idAssignment);
    }

    public void deleteAllAssignmentRelatedToCourse(Long idCourse) {
        Long idProfessor = getIdProfessorFromToken();
        assignmentRepository.deleteAllAssignmentRelatedToCourse(idCourse, idProfessor);
    }
}
