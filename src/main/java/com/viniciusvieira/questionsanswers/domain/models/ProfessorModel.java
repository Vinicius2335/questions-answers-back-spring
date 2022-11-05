package com.viniciusvieira.questionsanswers.domain.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Table(name = "TB_PROFESSOR")
public class ProfessorModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "The id of the professor")
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfessor;

	@Schema(description = "The name of the professor")
    @Column(nullable = false)
    private String name;

	@Schema(description = "The email of the professor")
    @Column(nullable = false, unique = true)
    private String email;
    
}
