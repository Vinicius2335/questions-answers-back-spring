package com.viniciusvieira.questionsanswers.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.models.CourseModel;
import com.viniciusvieira.questionsanswers.models.ProfessorModel;

@Repository
public interface CourseRepository extends JpaRepository<CourseModel, Long> {
	Optional<CourseModel> findByIdAndProfessor(Long id, ProfessorModel professor);
}
