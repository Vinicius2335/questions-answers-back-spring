package com.viniciusvieira.questionsanswers.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorModel, Long> {
	ProfessorModel findByEmail(String email); 
}
