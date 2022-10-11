package com.viniciusvieira.questionsanswers.repositories;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.models.CourseModel;


@Repository
public interface CourseRepository extends JpaRepository<CourseModel, Long> {
	
	@Query(value = "SELECT * FROM TB_COURSE c WHERE c.id_course = :id "
			+ "AND c.professor_id = :professor AND c.enabled = true", nativeQuery = true)
	Optional<CourseModel> findOneCourse(@Param("id") Long idCourse, @Param("professor") Long idProfessor);
	
	@Query(value = "SELECT * FROM TB_COURSE c WHERE c.name LIKE %:name% AND"
			+ " c.professor_id = :id_professor AND c.enabled = true", nativeQuery = true)
	List<CourseModel> listCoursesByName(
			@Param("name") String name,
			@Param("id_professor") Long idProfessor
	);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_COURSE c SET c.enabled = false WHERE c.id_course = :id "
			+ "AND e.professor = :professor", nativeQuery = true)
	void deleteById(@Param("id")Long idCourse, @Param("professor") Long idProfessor);

	
}
