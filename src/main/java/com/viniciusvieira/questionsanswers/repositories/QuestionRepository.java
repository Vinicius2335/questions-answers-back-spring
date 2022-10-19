package com.viniciusvieira.questionsanswers.repositories;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.models.QuestionModel;


@Repository
public interface QuestionRepository extends JpaRepository<QuestionModel, Long> {
	
	@Query(value = "SELECT * FROM TB_QUESTION q WHERE q.id_question = :id "
			+ "AND q.professor_id = :professor AND q.enabled = true", nativeQuery = true)
	Optional<QuestionModel> findOneQuestion(@Param("id") Long idQuestion, @Param("professor") Long idProfessor);
	
	@Query(value = "SELECT * FROM TB_QUESTION q WHERE q.course_id = :id_course AND q.title LIKE %:title% AND"
			+ " q.professor_id = :id_professor AND q.enabled = true", nativeQuery = true)
	List<QuestionModel> listQuestionByCourseAndTitle(
			@Param("id_course") Long idCourse,
			@Param("title") String title,
			@Param("id_professor") Long idProfessor
	);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_QUESTION q SET q.enabled = false WHERE q.id_question = :id "
			+ "AND q.professor_id = :professor", nativeQuery = true)
	void deleteById(@Param("id")Long idQuestion, @Param("professor") Long idProfessor);

	
}
