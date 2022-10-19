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
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idQuestion;
	
	@Column(nullable = false)
	private String title;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "course_id")
	private CourseModel course;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "professor_id")
	private ProfessorModel professor;
	
	@Column(columnDefinition = "boolean default true", nullable = false)
	private boolean enabled;
	
}
