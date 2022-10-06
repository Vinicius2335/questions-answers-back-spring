package com.viniciusvieira.questionsanswers.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.models.CourseModel;


@Repository
public interface CourseRepository extends JpaRepository<CourseModel, Long> {
	
//	@Query("SELECT c FROM TB_COURSE c WHERE c.id_course = ?1 and c.professor_id = ?#{principal.professor_id}")
//	Optional<CourseModel> findByIdAndProfessor(Long idCourse);
	
	CourseModel findByProfessor(Long idProfessor);
	
	@Query(value = "SELECT * FROM TB_COURSE c WHERE c.name LIKE %:name%", nativeQuery = true)
	List<CourseModel> listCourses(@Param("name") String name);
}
