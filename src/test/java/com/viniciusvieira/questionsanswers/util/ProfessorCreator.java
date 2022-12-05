package com.viniciusvieira.questionsanswers.util;

import com.viniciusvieira.questionsanswers.api.representation.models.ProfessorDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.ProfessorRequestBody;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;

public abstract class ProfessorCreator {
	public static ProfessorModel mockProfessor() {
		return ProfessorModel.builder()
				.idProfessor(1L)
				.name("willian")
				.email("willian@email.com").build();
	}

	public static ProfessorDto mockProfessorDto(){
		return ProfessorDto.builder()
				.idProfessor(1L)
				.email("willian@email.com")
				.name("willian")
				.build();
	}

	public static ProfessorRequestBody mockProfessorRequestBody(){
		return ProfessorRequestBody.builder()
				.name("willian")
				.email("willian@email.com")
				.build();
	}
}
