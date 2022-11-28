package com.viniciusvieira.questionsanswers.api.mappers.v1;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.viniciusvieira.questionsanswers.api.representation.models.QuestionAssignmentDto;
import com.viniciusvieira.questionsanswers.domain.models.QuestionAssignmentModel;
import org.mapstruct.Mapping;

@Mapper
public abstract class QuestionAssignmentMapper {
	public static final QuestionAssignmentMapper INSTANCE = Mappers.getMapper(QuestionAssignmentMapper.class);
	
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "idQuestionAssignment", ignore = true)
	@Mapping(target = "professor", ignore = true)
	public abstract QuestionAssignmentModel toQuestionAssignmentModel(QuestionAssignmentDto questionAssignmentDto);
}
