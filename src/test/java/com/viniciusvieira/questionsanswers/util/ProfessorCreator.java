package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;

public abstract class ProfessorCreator {
	
	public static ProfessorModel mockProfessor() {
		return ProfessorModel.builder()
				.idProfessor(1L)
				.name("willian")
				.email("willian@email.com").build();
	}
}
