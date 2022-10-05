package com.viniciusvieira.questionsanswers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.models.ProfessorModel;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorModel, Long> {
	ProfessorModel findByEmail(String email); 
}
