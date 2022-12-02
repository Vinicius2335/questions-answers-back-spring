package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.StudentModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DeleteUserService {
    private final ApplicationUserRepository applicationUserRepository;

    @Transactional
    public void deleteStudent(StudentModel studentModel){
        ApplicationUserModel student = applicationUserRepository.findByStudent(studentModel);
        applicationUserRepository.delete(student);
    }

    @Transactional
    public void deleteProfessor(ProfessorModel professorModel){
        ApplicationUserModel professor = applicationUserRepository.findByProfessor(professorModel);
        applicationUserRepository.delete(professor);
    }
}
