package com.viniciusvieira.questionsanswers.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfessor;

    @NotEmpty(message = "The field name cannot be empty")
    private String name;

    // Todo testar dps se valida com o campo sendo vazio/null
    @Email(message = "The email is not valid")
    @Column(unique = true)
    private String email;
}
