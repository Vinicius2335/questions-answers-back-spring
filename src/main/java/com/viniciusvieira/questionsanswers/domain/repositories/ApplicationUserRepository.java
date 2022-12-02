package com.viniciusvieira.questionsanswers.domain.repositories;

import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUserModel, Long> {
    ApplicationUserModel findByUsername(String username);

    ApplicationUserModel findByStudent(StudentModel studentModel);
    ApplicationUserModel findByProfessor(ProfessorModel professorModel);

}

//TEST