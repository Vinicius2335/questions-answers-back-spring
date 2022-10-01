package com.viniciusvieira.questionsanswers.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
public class ApplicationUserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @OneToOne
    @JoinColumn(name = "professor_id")
    private ProfessorModel professor;

    public ApplicationUserModel(ApplicationUserModel applicationUser){
        this.username = applicationUser.getUsername();
        this.password = applicationUser.getPassword();
        this.professor = applicationUser.getProfessor();
    }
}
