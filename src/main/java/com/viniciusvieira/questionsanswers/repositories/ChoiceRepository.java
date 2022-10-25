package com.viniciusvieira.questionsanswers.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.models.QuestionModel;

@Repository
public interface ChoiceRepository extends JpaRepository<ChoiceModel, Long> {
	@Query(value = "SELECT * FROM TB_CHOICE c WHERE c.id_choice = :id "
			+ "AND c.professor_id = :professor AND c.enabled = true", nativeQuery = true)
	Optional<ChoiceModel> findOneChoice(@Param("id") Long idChoice, @Param("professor") Long idProfessor);
	
	@Query(value = "SELECT * FROM TB_CHOICE c WHERE c.question_id = :question_id AND"
			+ " c.professor_id = :professor_id AND c.enabled = true", nativeQuery = true)
	List<ChoiceModel> listChoiceByQuestionId(
			@Param("question_id") Long idQuestion,
			@Param("professor_id") Long idProfessor
	);
	
	// TODO: sei se vai dar certo
	@Modifying
	@Query(value = "UPDATE TB_CHOICE c SET c.correct_answer = false WHERE c.id_choice <> :choice AND c.question_id = :question AND"
			+ " c.professor_id = :professor_id AND c.enabled = true", nativeQuery = true)
	void updatedAllOtherChoicesCorrectAnswerToFalse(
			@Param("choice") ChoiceModel choice,
			@Param("question") QuestionModel question,
			@Param("professor_id") Long idProfessor
	);
	
	@Query(value = "SELECT * FROM TB_CHOICE c WHERE c.correct_answer <> true", nativeQuery = true)
	List<ChoiceModel> testUpdatedAllOtherChoicesCorrectAnswerToFalse();
	
	@Modifying
	@Query(value = "UPDATE TB_CHOICE c SET c.enabled = false WHERE c.id_choice = :id "
			+ "AND c.professor_id = :professor_id", nativeQuery = true)
	void deleteById(@Param("id") Long idChoice, @Param("professor_id") Long idProfessor);
	
	// cascade soft delete course -> question -> choice
	@Modifying
	@Query(value = "UPDATE TB_CHOICE c SET c.enabled = false WHERE c.question_id = :question_id "
			+ "AND c.professor_id = :professor_id", nativeQuery = true)
	void deleteAllChoicesRelatedToQuestion(
			@Param("question_id") Long idQuestion,
			@Param("professor_id") Long idProfessor
	);
}
