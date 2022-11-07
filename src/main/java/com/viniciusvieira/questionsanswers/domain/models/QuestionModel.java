package com.viniciusvieira.questionsanswers.domain.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "TB_QUESTION")
public class QuestionModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Schema(description = "The id of the question")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idQuestion;
	
	@Schema(description = "The title of the question")
	@Column(nullable = false)
	private String title;
	
	@Schema(description = "many-to-one relationship with course")
	@ManyToOne
	@JoinColumn(nullable = false, name = "course_id")
	private CourseModel course;
	
	@Schema(description = "many-to-one relationship with professor")
	@ManyToOne
	@JoinColumn(nullable = false, name = "professor_id")
	private ProfessorModel professor;
	
	@JsonIgnore
	@OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
	private List<ChoiceModel> choices;
	
	@Schema(description = "describes whether the question is enabled or not")
	@Column(columnDefinition = "boolean default true", nullable = false)
	private boolean enabled;
	
}
