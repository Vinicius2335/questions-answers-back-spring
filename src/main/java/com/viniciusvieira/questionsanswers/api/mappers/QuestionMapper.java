package com.viniciusvieira.questionsanswers.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.viniciusvieira.questionsanswers.api.representation.models.QuestionDto;
import com.viniciusvieira.questionsanswers.domain.models.QuestionModel;

@Mapper
public abstract class QuestionMapper {
	public static final QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);
	
	public abstract QuestionModel toQuestionModel(QuestionDto questionDto);
	
}
