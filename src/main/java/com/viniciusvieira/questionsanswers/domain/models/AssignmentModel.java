package com.viniciusvieira.questionsanswers.domain.models;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@Table(name = "TB_ASSIGNMENT")
public class AssignmentModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Schema(description = "The id of the assignment")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAssignment;
	
	@Schema(description = "The title of the assignment")
	@Column(nullable = false)
	private String title;
	
	@Schema(description = "The creation date of the assignment")
	@Column(nullable = false)
	private final LocalDateTime createdAt = LocalDateTime.now();
	
	@Schema(description = "many-to-one relationship with course")
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, name = "course_id")
	private CourseModel course;
	
	@Schema(description = "many-to-one relationship with professor")
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, name = "professor_id")
	private ProfessorModel professor;
	
	@Schema(description = "describes whether the assignment is enabled or not")
	@Column(nullable = false, columnDefinition = "boolean default true")
	private boolean enabled;
	
	@Schema(description = "access code that the professor will provide for the student to take the exam")
	@Column(nullable = false, unique = true)
	private String accessCode;

}
