package com.viniciusvieira.questionsanswers.domain.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Table(name = "TB_QUESTION_ASSIGNMENT")
@Tag(description = "Questions associated with assignments, the sum of all grades for one assignment must be equals"
		+ "to 100 in order to be used by the students", name = "QuestionAssignment Model")
public class QuestionAssignmentModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description =  "The id of the questionAssignment")
	private Long idQuestionAssignment;
	
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, name = "question_id")
	@Schema(description = "The question for associated with that assignment")
	private QuestionModel question;
	
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, name = "assignment_id")
	@Schema(description = "The assignment that the question belongs to")
	private AssignmentModel assignment;
	
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false, name = "professor_id")
	@Schema(description = "many-to-one relationship with professor")
	private ProfessorModel professor;
	
	@Column(nullable = false)
	private double grade;
	
	@Column(nullable = false, columnDefinition = "boolean default true")
	@Schema(description = "describes whether the questionAssignment is enabled or not")
	private boolean enabled;
}
