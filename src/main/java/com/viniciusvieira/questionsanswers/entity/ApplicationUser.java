package com.viniciusvieira.questionsanswers.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The field username cannot be empty")
    @Column(unique = true)
    private String userName;

    @NotEmpty(message = "The field password cannot be empty")
    private String password;

    @OneToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;
}
