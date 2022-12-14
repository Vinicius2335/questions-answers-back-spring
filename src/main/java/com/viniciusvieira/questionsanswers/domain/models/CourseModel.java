package com.viniciusvieira.questionsanswers.domain.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "TB_COURSE")
public class CourseModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "The id of the course")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCourse;
	
	@Schema(description = "The name of the course")
	@Column(nullable = false, length = 150)
	private String name;
	
	@Schema(description = "many-to-one relationship with professor")
	@ManyToOne
	@JoinColumn(name = "professor_id", nullable = false)
	private ProfessorModel professor;
	
	@Schema(description = "Describes whether the course is enabled or not")
	@Column(columnDefinition = "boolean default true", nullable = false)
	private boolean enabled;
}

