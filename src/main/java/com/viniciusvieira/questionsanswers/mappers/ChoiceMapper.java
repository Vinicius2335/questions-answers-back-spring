package com.viniciusvieira.questionsanswers.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.viniciusvieira.questionsanswers.dtos.ChoiceDto;
import com.viniciusvieira.questionsanswers.models.ChoiceModel;

@Mapper
public abstract class ChoiceMapper {
	public static final ChoiceMapper INSTANCE = Mappers.getMapper(ChoiceMapper.class);
	
	public abstract ChoiceModel toChoiceModel(ChoiceDto choiceDto);
}
