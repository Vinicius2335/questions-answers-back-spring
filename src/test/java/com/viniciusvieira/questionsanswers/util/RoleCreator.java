package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.domain.enums.RoleNames;
import com.viniciusvieira.questionsanswers.domain.models.RoleModel;

public abstract class RoleCreator {
	public static RoleModel mockRoleProfessor() {
		return RoleModel.builder()
				.idRole(1L)
				.roleName(RoleNames.ROLE_PROFESSOR)
				.build();
	}
	
	public static RoleModel mockRoleStudent() {
		return RoleModel.builder()
				.idRole(2L)
				.roleName(RoleNames.ROLE_STUDENT)
				.build();
	}
}
