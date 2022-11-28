package com.viniciusvieira.questionsanswers.domain.services;

import org.springframework.stereotype.Service;

import com.viniciusvieira.questionsanswers.domain.exception.ProfessorNotFoundException;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ProfessorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfessorService {
	private final ProfessorRepository professorRepository;
	
	public ProfessorModel findByIdOrThrowProfessorNotFoundException(Long id) {
		return professorRepository.findById(id)
				.orElseThrow(() -> new ProfessorNotFoundException("Professor Not Found"));
	}
}
