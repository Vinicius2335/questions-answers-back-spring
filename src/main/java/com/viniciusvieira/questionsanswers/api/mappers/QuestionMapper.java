package com.viniciusvieira.questionsanswers.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.viniciusvieira.questionsanswers.api.representation.models.QuestionDto;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;
import org.mapstruct.Mapping;

@Mapper
public abstract class QuestionMapper {
	public static final QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);
	
	@Mapping(target = "choices", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "idQuestion", ignore = true)
	@Mapping(target = "professor", ignore = true)
	public abstract QuestionModel toQuestionModel(QuestionDto questionDto);
	
}
