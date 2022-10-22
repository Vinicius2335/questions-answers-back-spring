package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.Enums.RoleNames;
import com.viniciusvieira.questionsanswers.models.RoleModel;

public class RoleCreator {
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
