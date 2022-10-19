package com.viniciusvieira.questionsanswers.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.viniciusvieira.questionsanswers.dtos.QuestionDto;
import com.viniciusvieira.questionsanswers.models.QuestionModel;

@Mapper
public abstract class QuestionMapper {
	public static final QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);
	
	public abstract QuestionModel toQuestionModel(QuestionDto questionDto);
	
}
