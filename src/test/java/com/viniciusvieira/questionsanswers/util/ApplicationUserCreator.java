package com.viniciusvieira.questionsanswers.util;

import java.util.List;

import com.viniciusvieira.questionsanswers.api.representation.models.ApplicationUserDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.ApplicationUserRequestBody;
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

	public static ApplicationUserRequestBody mockApplicationUserRequestBody(){
		return ApplicationUserRequestBody.builder()
				.username("vinicius")
				.password("vinicius")
				.build();
	}

	public static ApplicationUserDto mockApplicationUserDto(){
		return ApplicationUserDto.builder()
				.idApplicationUser(2L)
				.password(new BCryptPasswordEncoder().encode("vinicius"))
				.username("goku")
				.build();
	}
}
