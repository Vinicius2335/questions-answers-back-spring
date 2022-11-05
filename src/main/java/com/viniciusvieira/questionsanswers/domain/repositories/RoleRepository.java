package com.viniciusvieira.questionsanswers.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viniciusvieira.questionsanswers.domain.models.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, Long>{

}
