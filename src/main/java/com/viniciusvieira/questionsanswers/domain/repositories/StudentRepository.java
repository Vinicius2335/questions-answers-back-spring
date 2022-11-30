package com.viniciusvieira.questionsanswers.domain.repositories;

import com.viniciusvieira.questionsanswers.domain.models.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentModel, Long> {
    List<StudentModel> findByNameContaining(String name);
}

//TEST