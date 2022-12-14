package com.viniciusvieira.questionsanswers.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.domain.models.ChoiceModel;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;

@Repository
public interface ChoiceRepository extends JpaRepository<ChoiceModel, Long> {
	@Query(value = "SELECT * FROM TB_CHOICE c WHERE c.id_choice = :id "
			+ "AND c.professor_id = :professor AND c.enabled = true", nativeQuery = true)
	Optional<ChoiceModel> findOneChoice(@Param("id") Long idChoice, @Param("professor") Long idProfessor);

	@Query(value = "SELECT * FROM TB_CHOICE c WHERE c.question_id = :question_id AND"
			+ " c.professor_id = :professor_id AND c.enabled = true", nativeQuery = true)
	List<ChoiceModel> listChoiceByQuestionId(@Param("question_id") Long idQuestion,
			@Param("professor_id") Long idProfessor);
	
	@Query(value = "SELECT * FROM TB_CHOICE c WHERE c.question_id in :questions_id AND "
			+ "c.enabled = true", nativeQuery = true)
	List<ChoiceModel> listChoiceByQuestionsIdForStudent(@Param("questions_id") List<Long> idQuestions);
	
	@Query(value = "SELECT * FROM TB_CHOICE c WHERE c.question_id = :id_question AND c.title LIKE %:title% AND"
			+ " c.professor_id = :id_professor AND c.enabled = true", nativeQuery = true)
	List<ChoiceModel> listChoiceByQuestionAndTitle(
			@Param("id_question") Long idQuestion,
			@Param("title") String title,
			@Param("id_professor") Long idProfessor
	);

	@Modifying
	@Query(value = "UPDATE TB_CHOICE c SET c.correct_answer = false WHERE c.id_choice <> :choice AND c.question_id = :question AND"
			+ " c.professor_id = :professor_id AND c.enabled = true", nativeQuery = true)
	void updatedAllOtherChoicesCorrectAnswerToFalse(@Param("choice") ChoiceModel choice,
			@Param("question") QuestionModel question, @Param("professor_id") Long idProfessor);

	@Query(value = "SELECT * FROM TB_CHOICE c WHERE c.correct_answer <> true", nativeQuery = true)
	List<ChoiceModel> testUpdatedAllOtherChoicesCorrectAnswerToFalse();

	@Modifying
	@Query(value = "UPDATE TB_CHOICE c SET c.enabled = false WHERE c.id_choice = :id "
			+ "AND c.professor_id = :professor_id", nativeQuery = true)
	void deleteById(@Param("id") Long idChoice, @Param("professor_id") Long idProfessor);

	// cascade soft delete course -> question -> choice
	@Modifying
	@Query(value = "UPDATE TB_CHOICE c SET c.enabled = false WHERE c.question_id = :question_id "
			+ "AND c.professor_id = :professor_id AND c.enabled = true", nativeQuery = true)
	void deleteAllChoicesRelatedToQuestion(@Param("question_id") Long idQuestion,
			@Param("professor_id") Long idProfessor);

	@Query(value = "UPDATE TB_CHOICE c SET c.enabled = false WHERE c.question_id IN "
			+ "(SELECT q.id_question FROM TB_QUESTION q WHERE q.course_id = :course_id) AND"
			+ " c.professor_id = :professor_id AND c.enabled = true", nativeQuery = true)
	@Modifying
	void deleteAllChoicesRelatedToCourse(@Param("course_id")Long courseId,
			@Param("professor_id")Long professorId);
	
	@Query(value = "SELECT * FROM TB_CHOICE c WHERE c.enabled = false", nativeQuery = true)
	List<ChoiceModel> testFindAllEnabledFalse();
}
