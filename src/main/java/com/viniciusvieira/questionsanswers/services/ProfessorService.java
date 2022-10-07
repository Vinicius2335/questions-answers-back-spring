package com.viniciusvieira.questionsanswers.services;

import org.springframework.stereotype.Service;

import com.viniciusvieira.questionsanswers.excepiton.ProfessorNotFoundException;
import com.viniciusvieira.questionsanswers.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.repositories.ProfessorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfessorService {
	private final ProfessorRepository professorRepository;
	
	public ProfessorModel findByIdOrThrownProfessorNotFoundException(Long id) {
		return professorRepository.findById(id)
				.orElseThrow(() -> new ProfessorNotFoundException("Professor Not Found"));
	}
}
