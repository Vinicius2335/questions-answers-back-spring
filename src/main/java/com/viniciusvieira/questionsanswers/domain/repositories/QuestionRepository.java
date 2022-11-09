package com.viniciusvieira.questionsanswers.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;


@Repository
public interface QuestionRepository extends JpaRepository<QuestionModel, Long> {
	
	@Query(value = "SELECT * FROM TB_QUESTION q WHERE q.id_question = :id_question "
			+ "AND q.professor_id = :professor_id AND q.enabled = true", nativeQuery = true)
	Optional<QuestionModel> findOneQuestion(
			@Param("id_question") Long idQuestion, 
			@Param("professor_id") Long idProfessor
	);
	
	@Query(value = "SELECT * FROM TB_QUESTION q WHERE q.course_id = :course_id AND"
			+ " q.professor_id = :professor_id ", nativeQuery = true)
	QuestionModel findByIdCourse(
			@Param("course_id") Long idCourse,
			@Param("professor_id") Long idProfessor
	);
	
	@Query(value = "SELECT * FROM TB_QUESTION q WHERE q.course_id = :id_course AND q.title LIKE %:title% AND"
			+ " q.professor_id = :id_professor AND q.enabled = true", nativeQuery = true)
	List<QuestionModel> listQuestionByCourseAndTitle(
			@Param("id_course") Long idCourse,
			@Param("title") String title,
			@Param("id_professor") Long idProfessor
	);
	
	@Modifying
	@Query(value = "UPDATE TB_QUESTION q SET q.enabled = false WHERE q.id_question = :id "
			+ "AND q.professor_id = :professor_id", nativeQuery = true)
	void deleteById(@Param("id")Long idQuestion, @Param("professor_id") Long idProfessor);
	
	// cascade soft delete course -> question
	@Modifying
	@Query(value = "UPDATE TB_QUESTION q SET q.enabled = false WHERE q.course_id = :course_id "
			+ "AND q.professor_id = :professor_id", nativeQuery = true)
	void deleteAllQuestionsRelatedToCouse(
			@Param("course_id") Long idCourse, 
			@Param("professor_id") Long idProfessor
	);
	
	@Query(value = "SELECT * FROM TB_QUESTION q WHERE q.course_id = :course_id AND q.id_question NOT IN "
			+ "(SELECT qa.question_id FROM TB_QUESTION_ASSIGNMENT qa WHERE qa.assignment_id = :assignment_id AND "
			+ "qa.professor_id = :professor_id AND q.enabled = true) AND "
			+ "q.professor_id = :professor_id AND q.enabled = true",
			nativeQuery = true)
	@Transactional
	List<QuestionModel> findAllQuestionsByCourseNotAssociatedWithAnAssignment(
			@Param("course_id") Long courseId,
			@Param("assignment_id") Long assignmentId,
			@Param("professor_id") Long professorId
	);
	
}
