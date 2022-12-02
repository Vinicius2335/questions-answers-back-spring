package com.viniciusvieira.questionsanswers.domain.services;

import com.viniciusvieira.questionsanswers.api.representation.requests.ApplicationUserRequestBody;
import com.viniciusvieira.questionsanswers.domain.enums.RoleNames;
import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.models.RoleModel;
import com.viniciusvieira.questionsanswers.domain.models.StudentModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class InsertUserService {
    private final ApplicationUserRepository applicationUserRepository;

    public static final RoleModel ROLE_STUDENT = RoleModel.builder().idRole(2L).roleName(RoleNames.ROLE_STUDENT).build();
    public static final RoleModel ROLE_PROFESSOR = RoleModel.builder().idRole(1L).roleName(RoleNames.ROLE_PROFESSOR).build();

    public ApplicationUserModel insertStudent(StudentModel student, ApplicationUserRequestBody applicationUserRequestBody){
        ApplicationUserModel applicationUser = ApplicationUserModel.builder()
                .username(applicationUserRequestBody.getUsername())
                .password(new BCryptPasswordEncoder().encode(applicationUserRequestBody.getPassword()))
                .student(student)
                .roles(List.of(ROLE_STUDENT))
                .build();

        return applicationUserRepository.save(applicationUser);
    }

    public ApplicationUserModel insertProfessor(ProfessorModel professor,
                                                ApplicationUserRequestBody applicationUserRequestBody){
        ApplicationUserModel applicationUser = ApplicationUserModel.builder()
                .username(applicationUserRequestBody.getUsername())
                .password(new BCryptPasswordEncoder().encode(applicationUserRequestBody.getPassword()))
                .professor(professor)
                .roles(List.of(ROLE_PROFESSOR))
                .build();

        return applicationUserRepository.save(applicationUser);
    }
}

//TEST