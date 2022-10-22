package com.viniciusvieira.questionsanswers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viniciusvieira.questionsanswers.models.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, Long>{

}
