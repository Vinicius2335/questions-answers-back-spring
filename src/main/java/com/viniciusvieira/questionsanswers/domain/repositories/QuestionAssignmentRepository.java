package com.viniciusvieira.questionsanswers.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.domain.models.QuestionAssignmentModel;

@Repository
public interface QuestionAssignmentRepository extends JpaRepository<QuestionAssignmentModel, Long> {
	@Query(value = "SELECT * FROM TB_QUESTION_ASSIGNMENT qa WHERE qa.id_question_assignment = :id_question_assignment "
			+ "AND qa.professor_id = :professor AND qa.enabled = true", nativeQuery = true)
	Optional<QuestionAssignmentModel> findOneQuestionAssignment(
			@Param("id_question_assignment") Long idQuestionAssignment, 
			@Param("professor") Long professorId
	);
	
	@Query(value = "SELECT * FROM TB_QUESTION_ASSIGNMENT qa WHERE qa.question_id = :question_id "
			+ "AND qa.assignment_id = :assignment_id "
			+ "AND qa.professor_id = :professor_id AND qa.enabled = true", nativeQuery = true)
	List<QuestionAssignmentModel> listQuestionAssignmentByQuestionAndAssignment(
			@Param("question_id") Long questionId,
			@Param("assignment_id") Long assignmentId,
			@Param("professor_id") Long professor_id
	);
	
	@Query(value = "SELECT * FROM TB_QUESTION_ASSIGNMENT qa WHERE qa.assignment_id = :assignment_id "
			+ "AND qa.professor_id = :professor_id AND qa.enabled = true", nativeQuery = true)
	List<QuestionAssignmentModel> listQuestionAssignmentByAssignmentId(
			@Param("assignment_id") Long assignmentId,
			@Param("professor_id") Long professor_id
	);
	
	@Modifying
	@Query(value = "UPDATE TB_QUESTION_ASSIGNMENT qa SET qa.enabled = false "
			+ "WHERE qa.id_question_assignment = :id_question_assignment "
			+ "AND qa.professor_id = :professor_id", nativeQuery = true)
	void deleteById(@Param("id_question_assignment")Long idQuestionAssignment,
			@Param("professor_id") Long professorId);
	
	@Modifying
	@Query(value = "UPDATE TB_QUESTION_ASSIGNMENT qa SET qa.enabled = false WHERE qa.assignment_id IN "
			+ "(SELECT a.id_assignment FROM TB_ASSIGNMENT a WHERE a.course_id = :course_id) "
			+ "AND qa.professor_id = :professor_id AND qa.enabled = true", nativeQuery = true)
	void deleteAllQuestionAssignmentRelatedToCourse(
			@Param("course_id") Long courseId,
			@Param("professor_id") Long professorId
	);
	
	@Modifying
	@Query(value = "UPDATE TB_QUESTION_ASSIGNMENT qa SET qa.enabled = false WHERE qa.assignment_id = :assignment_id "
			+ "AND qa.professor_id = :professor_id AND qa.enabled = true", nativeQuery = true)
	void deleteAllQuestionAssignmentRelatedToAssignment(
			@Param("assignment_id") Long assignmentId,
			@Param("professor_id") Long professorId
	);
	
	@Modifying
	@Query(value = "UPDATE TB_QUESTION_ASSIGNMENT qa SET qa.enabled = false WHERE qa.question_id = :question_id "
			+ "AND qa.professor_id = :professor_id AND qa.enabled = true", nativeQuery = true)
	void deleteAllQuestionAssignmentRelatedToQuestion(
			@Param("question_id") Long questionId,
			@Param("professor_id") Long professorId
	);
	
}
