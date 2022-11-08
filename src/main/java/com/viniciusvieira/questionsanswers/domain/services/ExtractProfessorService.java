package com.viniciusvieira.questionsanswers.domain.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.viniciusvieira.questionsanswers.domain.models.ApplicationUserModel;
import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import com.viniciusvieira.questionsanswers.domain.repositories.ApplicationUserRepository;

import lombok.RequiredArgsConstructor;

//TEST
@Service
@RequiredArgsConstructor
public class ExtractProfessorService {
	private final ApplicationUserRepository applicationUserRepository;
	
	public ProfessorModel extractProfessorFromToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (String)authentication.getPrincipal();
		ApplicationUserModel user = applicationUserRepository.findByUsername(username);
		return user.getProfessor();
	}
}
