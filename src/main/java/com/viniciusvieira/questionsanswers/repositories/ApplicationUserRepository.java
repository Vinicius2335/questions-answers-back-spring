package com.viniciusvieira.questionsanswers.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.viniciusvieira.questionsanswers.models.ApplicationUserModel;

@Repository
public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUserModel, Long> {
    ApplicationUserModel findByUsername(String username);
}
