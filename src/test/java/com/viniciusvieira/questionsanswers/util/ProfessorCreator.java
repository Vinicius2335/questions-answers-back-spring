package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.models.ProfessorModel;

public class ProfessorCreator {
	
	public static ProfessorModel mockProfessor() {
		return ProfessorModel.builder()
				.idProfessor(1L)
				.name("willian")
				.email("willian@email.com").build();
	}
}
