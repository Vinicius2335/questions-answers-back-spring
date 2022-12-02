package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.StudentModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DeleteUserService {
    private final ApplicationUserRepository applicationUserRepository;

    public void deleteStudent(StudentModel studentModel){
        ApplicationUserModel student = applicationUserRepository.findByStudent(studentModel);
        applicationUserRepository.delete(student);
    }

    public void deleteProfessor(ProfessorModel professorModel){
        ApplicationUserModel professor = applicationUserRepository.findByProfessor(professorModel);
        applicationUserRepository.delete(professor);
    }
}

//TEST