package com.viniciusvieira.questionsanswers.util;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;

public abstract class ApplicationUserCreator {
	public static ApplicationUserModel mockUserProfessor() {
		return ApplicationUserModel.builder()
				.idUser(1L)
				.username("vinicius")
				.password(new BCryptPasswordEncoder().encode("devdojo"))
				.professor(ProfessorCreator.mockProfessor())
				.roles(List.of(RoleCreator.mockRoleProfessor()))
				.build();
	}
	
	public static ApplicationUserModel mockUserStudent() {
		return ApplicationUserModel.builder()
				.idUser(2L)
				.username("goku")
				.password(new BCryptPasswordEncoder().encode("vinicius"))
				.professor(ProfessorCreator.mockProfessor())
				.roles(List.of(RoleCreator.mockRoleStudent()))
				.build();
	}
}
