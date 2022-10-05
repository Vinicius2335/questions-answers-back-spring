package com.viniciusvieira.questionsanswers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.models.ApplicationUserModel;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUserModel, Long> {
    ApplicationUserModel findByUsername(String username);
}
