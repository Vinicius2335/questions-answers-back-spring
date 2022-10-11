package com.viniciusvieira.questionsanswers.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.models.CourseModel;


@Repository
public interface CourseRepository extends JpaRepository<CourseModel, Long> {
	
	CourseModel findByProfessor(Long idProfessor);
	
	@Query(value = "SELECT * FROM TB_COURSE c WHERE c.name LIKE %:name%", nativeQuery = true)
	List<CourseModel> listCourses(@Param("name") String name);
	
	@Query(value = "SELECT * FROM TB_COURSE c WHERE c.name LIKE %:name% AND"
			+ " c.professor_id = :id_professor", nativeQuery = true)
	List<CourseModel> listCoursesByName(
			@Param("name") String name,
			@Param("id_professor") Long idProfessor
	);
}
