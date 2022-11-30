package com.viniciusvieira.questionsanswers.domain.repositories;

import com.viniciusvieira.questionsanswers.domain.models.ProfessorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorModel, Long> {
	ProfessorModel findByEmail(String email);
	List<ProfessorModel> findByNameContaining(String name);
}
//TEST