package com.viniciusvieira.questionsanswers.models;

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
@Table(name = "TB_CHOICE")
public class ChoiceModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "The id of the choice")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idChoice;
	
	@Schema(description = "The title of the choice")
	@Column(nullable = false)
	private String title;
	
	// TODO: ??
	@Schema(description = "Correct answer for the associated question, you can have only one correct answer "
			+ "per question")
	@Column(nullable = false)
	private boolean correctAnswer;
	
	@Schema(description = "many-to-one relationship with question")
	// optional false = relacionamento não nulo sempre deve existir.
	@ManyToOne(optional = false)
	@JoinColumn(name = "question_id", nullable = false)
	private QuestionModel question;
	
	@Schema(description = "many-to-one relationship with professor")
	@ManyToOne(optional = false)
	@JoinColumn(name = "professor_id", nullable = false)
	private ProfessorModel professor;
	
	@Schema(description = "describes whether the choice is enabled or not")
	@Column(nullable = false, columnDefinition = "boolean default true")
	private boolean enabled;
}
