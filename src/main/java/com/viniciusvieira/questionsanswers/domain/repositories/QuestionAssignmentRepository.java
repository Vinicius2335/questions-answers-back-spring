package com.viniciusvieira.questionsanswers.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.domain.models.QuestionAssignmentModel;

@Repository
public interface QuestionAssignmentRepository extends JpaRepository<QuestionAssignmentModel, Long> {
	@Query(value = "SELECT * FROM TB_QUESTION_ASSIGNMENT q WHERE q.id_question_assignment = :id_question_assignment "
			+ "AND q.professor_id = :professor AND q.enabled = true", nativeQuery = true)
	Optional<QuestionAssignmentModel> findOneQuestionAssignment(
			@Param("id_question_assignment") Long idQuestionAssignment, 
			@Param("professor") Long idProfessor
	);
}
