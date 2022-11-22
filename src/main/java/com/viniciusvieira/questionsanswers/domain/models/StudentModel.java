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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_STUDENT")
public class StudentModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "The id of the student")
	private Long idStudent;
	
	@Schema(description = "The name of the student")
    @Column(nullable = false)
    private String name;

	@Schema(description = "The email of the student")
    @Column(nullable = false, unique = true)
    private String email;
	
	@Schema(description = "describes whether the choice is enabled or not")
	@Column(nullable = false, columnDefinition = "boolean default true")
	private boolean enabled;
}
