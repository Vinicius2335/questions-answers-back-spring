package com.viniciusvieira.questionsanswers.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
public class ProfessorModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfessor;

    @NotEmpty(message = "The field name cannot be empty")
    @Column
    private String name;

    @Email(message = "The email is not valid")
    @Column(unique = true)
    private String email;
}
