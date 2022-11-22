package com.viniciusvieira.questionsanswers.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.domain.models.AssignmentModel;

@Repository
public interface AssignmentRepository extends JpaRepository<AssignmentModel, Long> {
	
	@Query(value = "SELECT * FROM TB_ASSIGNMENT a WHERE a.id_assignment = :id_assignment "
			+ "AND a.professor_id = :professor AND a.enabled = true", nativeQuery = true)
	Optional<AssignmentModel> findOneAssignment(
			@Param("id_assignment") Long idAssignment, 
			@Param("professor") Long idProfessor
	);

	@Query(value = "SELECT * FROM TB_ASSIGNMENT a WHERE a.course_id = :id_course AND a.title LIKE %:title% AND"
			+ " a.professor_id = :id_professor AND a.enabled = true", nativeQuery = true)
	List<AssignmentModel> listAssignmentByCourseAndTitle(
			@Param("id_course") Long idCourse,
			@Param("title") String title,
			@Param("id_professor") Long idProfessor
	);


	@Modifying
	@Query(value = "UPDATE TB_ASSIGNMENT a SET a.enabled = false WHERE a.id_assignment = :id_assignment "
			+ "AND a.professor_id = :professor_id", nativeQuery = true)
	void deleteById(@Param("id_assignment") Long idAssignment, @Param("professor_id") Long idProfessor);

	// cascade soft delete course -> assignment
	@Modifying
	@Query(value = "UPDATE TB_ASSIGNMENT a SET a.enabled = false WHERE a.course_id = :course_id "
			+ "AND a.professor_id = :professor_id AND a.enabled = true", nativeQuery = true)
	void deleteAllAssignmentRelatedToCourse(@Param("course_id") Long idCourse,
			@Param("professor_id") Long idProfessor);

	@Query(value = "SELECT * FROM TB_ASSIGNMENT a WHERE a.enabled = false", nativeQuery = true)
	List<AssignmentModel> testFindAllEnabledFalse();
	
	@Query(value = "SELECT * FROM TB_ASSIGNMENT a WHERE a.course_id = :course_id AND "
			+ "a.access_code = :access_code AND "
			+ "a.professor_id = :professor_id AND a.enabled = false", nativeQuery = true)
	Optional<AssignmentModel> accessCodeExistsForCourse(
			@Param("access_code") String accessCode,
			@Param("course_id") Long courseId,
			@Param("professor_id") Long professorId
	);
}
